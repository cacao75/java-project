package cryptography;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.Arrays;
import javax.crypto.*;
import javax.crypto.Mac;
import javax.crypto.spec.*;

import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.KDF2BytesGenerator;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemReader;

public class EciesAlice {
    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // 1. Bob's public key 로딩
        PublicKey bobPubKey;
        try (PemReader pemReader = new PemReader(new FileReader("v:/lab_local/tmp/bob_public.pem"))) {
            byte[] keyBytes = pemReader.readPemObject().getContent();
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(keyBytes);
            bobPubKey = keyFactory.generatePublic(pubKeySpec);
        }

        // 2. Ephemeral 키 쌍 생성 (Alice 임시키)
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECDH", "BC");
        kpg.initialize(ECNamedCurveTable.getParameterSpec("secp256r1"));
        KeyPair ephKP = kpg.generateKeyPair();

        KeyAgreement ecdh = KeyAgreement.getInstance("ECDH");
        ecdh.init(ephKP.getPrivate());
        ecdh.doPhase(bobPubKey, true);
        byte[] sharedSecret = ecdh.generateSecret();

        // 3. KDF2 with SHA-256: 48 bytes (16 AES + 32 HMAC)
        KDF2BytesGenerator kdf = new KDF2BytesGenerator(new SHA256Digest());
        kdf.init(new ISO18033KDFParameters(sharedSecret));
        byte[] keyMaterial = new byte[48];
        kdf.generateBytes(keyMaterial, 0, 48);
        byte[] aesKey = Arrays.copyOfRange(keyMaterial, 0, 16);
        byte[] hmacKey = Arrays.copyOfRange(keyMaterial, 16, 48);

        // 4. AES-CBC + PKCS7Padding
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(aesKey, "AES"), new IvParameterSpec(iv));
        String message = "This is a secret message from Alice.";
        byte[] ciphertext = cipher.doFinal(message.getBytes());

        // 5. HMAC-SHA256
        Mac hmac = Mac.getInstance("HmacSHA256");
        hmac.init(new SecretKeySpec(hmacKey, "HmacSHA256"));
        byte[] mac = hmac.doFinal(ciphertext);

        // 6. 암호문 저장 (ephemeralPubKey | iv | ciphertext | mac)
        byte[] ephPubBytes = ephKP.getPublic().getEncoded();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(ephPubBytes.length);
        baos.write(ephPubBytes);
        baos.write(iv);
        baos.write(ciphertext);
        baos.write(mac);

        byte[] payload = baos.toByteArray();
        try (FileOutputStream fos = new FileOutputStream("v:/lab_local/tmp/encrypted_payload.bin")) {
            fos.write(payload);
        }

        // 7. 디버그 출력
        System.out.println("Ephemeral Public Key (len=" + ephPubBytes.length + "): " + bytesToHex(ephPubBytes));
        System.out.println("IV: " + bytesToHex(iv));
        System.out.println("AES Key: " + bytesToHex(aesKey));
        System.out.println("Ciphertext: " + bytesToHex(ciphertext));
        System.out.println("HMAC: " + bytesToHex(mac));
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes)
            sb.append(String.format("%02X", b));
        return sb.toString();
    }
}
