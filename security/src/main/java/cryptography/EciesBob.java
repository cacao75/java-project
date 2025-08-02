package cryptography;

import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.*;
import java.util.Arrays;
import javax.crypto.*;
import javax.crypto.spec.*;

import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.KDF2BytesGenerator;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemReader;

public class EciesBob {
    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // 1. Private key 로딩
        PrivateKey privKey;
        try (PemReader pemReader = new PemReader(new FileReader("v:/lab_local/tmp/bob_private_pkcs8.pem"))) {
            byte[] keyBytes = pemReader.readPemObject().getContent();
            KeyFactory kf = KeyFactory.getInstance("EC");
            PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(keyBytes);
            privKey = kf.generatePrivate(privSpec);
        }

        // 2. 암호문 로딩
        byte[] payload = Files.readAllBytes(new File("v:/lab_local/tmp/encrypted_payload.bin").toPath());
        int ephPubLen = payload[0] & 0xFF;
        byte[] ephPubBytes = Arrays.copyOfRange(payload, 1, 1 + ephPubLen);
        byte[] iv = Arrays.copyOfRange(payload, 1 + ephPubLen, 1 + ephPubLen + 16);
        byte[] ciphertext = Arrays.copyOfRange(payload, 1 + ephPubLen + 16, payload.length - 32);
        byte[] mac = Arrays.copyOfRange(payload, payload.length - 32, payload.length);

        KeyFactory kf = KeyFactory.getInstance("EC");
        X509EncodedKeySpec ephPubSpec = new X509EncodedKeySpec(ephPubBytes);
        PublicKey ephPubKey = kf.generatePublic(ephPubSpec);

        // 3. ECDH
        KeyAgreement ecdh = KeyAgreement.getInstance("ECDH");
        ecdh.init(privKey);
        ecdh.doPhase(ephPubKey, true);
        byte[] sharedSecret = ecdh.generateSecret();

        // 4. KDF2
        KDF2BytesGenerator kdf = new KDF2BytesGenerator(new SHA256Digest());
        kdf.init(new ISO18033KDFParameters(sharedSecret));
        byte[] keyMaterial = new byte[48];
        kdf.generateBytes(keyMaterial, 0, 48);
        byte[] aesKey = Arrays.copyOfRange(keyMaterial, 0, 16);
        byte[] hmacKey = Arrays.copyOfRange(keyMaterial, 16, 48);

        // 5. HMAC 검증
        Mac hmac = Mac.getInstance("HmacSHA256");
        hmac.init(new SecretKeySpec(hmacKey, "HmacSHA256"));
        byte[] calcMac = hmac.doFinal(ciphertext);
        if (!Arrays.equals(mac, calcMac)) {
            throw new SecurityException("MAC 검증 실패");
        }

        // 6. 복호화
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(aesKey, "AES"), new IvParameterSpec(iv));
        byte[] plaintext = cipher.doFinal(ciphertext);

        // 7. 출력
        System.out.println("Ephemeral Public Key: " + bytesToHex(ephPubBytes));
        System.out.println("IV: " + bytesToHex(iv));
        System.out.println("AES Key: " + bytesToHex(aesKey));
        System.out.println("Ciphertext: " + bytesToHex(ciphertext));
        System.out.println("HMAC: " + bytesToHex(mac));
        System.out.println("Recovered Message: " + new String(plaintext));

        try (FileOutputStream fos = new FileOutputStream("v:/lab_local/tmp/decrypted.txt")) {
            fos.write(plaintext);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02X", b));
        return sb.toString();
    }
}
