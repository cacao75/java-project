package cryptography;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.KDF2BytesGenerator;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECPoint;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

public class EciesAesGcmEx {

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // 1. Bob 키 쌍 생성 (공개키 --> Alice 전달)
        KeyPairGenerator bobKpg = KeyPairGenerator.getInstance("EC", "BC");
        bobKpg.initialize(new ECGenParameterSpec("secp256r1"));
        KeyPair bobKeyPair = bobKpg.generateKeyPair();
        ECPrivateKey bobPrivKey = (ECPrivateKey) bobKeyPair.getPrivate();
        ECPublicKey bobPubKey = (ECPublicKey) bobKeyPair.getPublic();

        System.out.println("🧑‍ [Step 1] Bob 공개키 생성 완료");

        // 2. Alice --> 데이터 암호화 후 전달
        System.out.println("\n👩 [Step 2] Alice: 암호화 시작");

        // Alice: ephemeral 키 쌍 생성
        KeyPairGenerator aliceKpg = KeyPairGenerator.getInstance("EC", "BC");
        aliceKpg.initialize(new ECGenParameterSpec("secp256r1"));
        KeyPair ephKeyPair = aliceKpg.generateKeyPair();
        ECPrivateKey ephPrivKey = (ECPrivateKey) ephKeyPair.getPrivate();
        ECPublicKey ephPubKey = (ECPublicKey) ephKeyPair.getPublic();

        // ECDH: 공유 비밀 생성
        KeyAgreement ka = KeyAgreement.getInstance("ECDH", "BC");
        ka.init(ephPrivKey);
        ka.doPhase(bobPubKey, true);
        byte[] sharedSecret = ka.generateSecret();

        // KDF2 + SHA-256 --> AES 128-bit 키
        KDF2BytesGenerator kdf = new KDF2BytesGenerator(new SHA256Digest());
        kdf.init(new ISO18033KDFParameters(sharedSecret));
        byte[] aesKey = new byte[16];
        kdf.generateBytes(aesKey, 0, aesKey.length);

        // AES-GCM 암호화
        String plaintext = "This is a secret message from Alice.";
        byte[] iv = new byte[12];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(aesKey, "AES"), new GCMParameterSpec(128, iv));
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes());

        // Ephemeral 공개키 인코딩 (0x04 || X || Y)
//        byte[] ephPubEncoded = ephPubKey.getEncoded(); // default: uncompressed 65B
        byte[] ephPubEncoded = ((org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey)ephPubKey).getQ().getEncoded(false);

        System.out.println("📦 Alice 전송 데이터:");
        System.out.println("- EphemeralPubKey (Base64): " + Base64.getEncoder().encodeToString(ephPubEncoded));
        System.out.println("- IV              (Base64): " + Base64.getEncoder().encodeToString(iv));
        System.out.println("- AES-Key         (Base64): " + Base64.getEncoder().encodeToString(aesKey));
        System.out.println("- Plaintext       (Base64): " + Base64.getEncoder().encodeToString(plaintext.getBytes()));
        System.out.println("- Ciphertext      (Base64): " + Base64.getEncoder().encodeToString(ciphertext));

        // 3. Bob → 수신 및 복호화
        System.out.println("\n📲 [Step 3] Bob: 복호화 시작");

        // Ephemeral 공개키 복원
        ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");
        ECPoint ephPoint = ecSpec.getCurve().decodePoint(ephPubEncoded);
        ECPublicKeyParameters ephPublicParams = new ECPublicKeyParameters(ephPoint,
                new ECDomainParameters(ecSpec.getCurve(), ecSpec.getG(), ecSpec.getN(), ecSpec.getH()));
        ECPrivateKeyParameters bobPrivParams = new ECPrivateKeyParameters(bobPrivKey.getS(),
                new ECDomainParameters(ecSpec.getCurve(), ecSpec.getG(), ecSpec.getN(), ecSpec.getH()));

        // ECDH
        org.bouncycastle.crypto.agreement.ECDHBasicAgreement agreement = new org.bouncycastle.crypto.agreement.ECDHBasicAgreement();
        agreement.init(bobPrivParams);
        BigInteger sharedSecretBob = agreement.calculateAgreement(ephPublicParams);
        byte[] sharedSecretBytes = sharedSecretBob.toByteArray();

        // KDF
        KDF2BytesGenerator kdfBob = new KDF2BytesGenerator(new SHA256Digest());
        kdfBob.init(new ISO18033KDFParameters(sharedSecretBytes));
        byte[] aesKeyBob = new byte[16];
        kdfBob.generateBytes(aesKeyBob, 0, aesKeyBob.length);

        // 복호화
        Cipher decipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
        decipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(aesKeyBob, "AES"), new GCMParameterSpec(128, iv));
        byte[] decrypted = decipher.doFinal(ciphertext);

        System.out.println("📦 Bob 수신 데이터:");
//        System.out.println("- IV              (Base64): " + Base64.getEncoder().encodeToString(iv));
        System.out.println("- AES-Key         (Base64): " + Base64.getEncoder().encodeToString(aesKeyBob));
//        System.out.println("- Plaintext       (Base64): " + Base64.getEncoder().encodeToString(plaintext.getBytes()));
        System.out.println("- Ciphertext      (Base64): " + Base64.getEncoder().encodeToString(decrypted));

        System.out.println("\n✅ 복호화 성공!");
        System.out.println("복호화된 메시지: " + new String(decrypted));
    }
}
