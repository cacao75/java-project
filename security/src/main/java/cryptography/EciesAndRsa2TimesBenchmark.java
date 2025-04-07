package cryptography;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Arrays;

public class EciesAndRsa2TimesBenchmark {

    private static final int REPEAT = 100;

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // Extend the test string to 300 bytes
        String base = "Insanity is doing the same thing over and over again and expecting different results. ";
        while (base.getBytes(StandardCharsets.UTF_8).length < 300) {
            base += base;
        }
        base = base.substring(0, 300);  // Cut it down to exactly 300 bytes
        byte[] message = base.getBytes(StandardCharsets.UTF_8);
        System.out.println("Original message length: " + message.length);

        System.out.println("\n=== ECIES ===");
        KeyPairGenerator ecKpg = KeyPairGenerator.getInstance("ECDH", "BC");
        ecKpg.initialize(new ECGenParameterSpec("prime256v1"));
        KeyPair ecPair = ecKpg.generateKeyPair();

        long totalEncryptTime = 0;
        long totalDecryptTime = 0;
        int totalEncryptedLength = 0;
        boolean allMatched = true;

        for (int i = 0; i < REPEAT; i++) {
            long start = System.nanoTime();
            byte[] encryptedEcies = encryptWithECIES(ecPair.getPublic(), message);
            long encryptEciesTime = System.nanoTime() - start;

            start = System.nanoTime();
            byte[] decryptedEcies = decryptWithECIES(ecPair.getPrivate(), encryptedEcies);
            long decryptEciesTime = System.nanoTime() - start;

            if (!Arrays.equals(message, decryptedEcies))
                allMatched = false;

            totalEncryptTime += encryptEciesTime;
            totalDecryptTime += decryptEciesTime;
            totalEncryptedLength += encryptedEcies.length;
        }

        System.out.printf("Average Encrypted Length: %.2f bytes%n", totalEncryptedLength / (double) REPEAT);
        System.out.printf("Average Encrypt Time: %.6f ms%n", totalEncryptTime / (REPEAT * 1_000_000.0));
        System.out.printf("Average Decrypt Time: %.6f ms%n", totalDecryptTime / (REPEAT * 1_000_000.0));
        System.out.println("All Decrypted == Original: " + allMatched);

        System.out.println("\n=== RSA ===");
        KeyPairGenerator rsaKpg = KeyPairGenerator.getInstance("RSA");
        rsaKpg.initialize(2048);
        KeyPair rsaPair = rsaKpg.generateKeyPair();

        // RSA 암호화: 150바이트씩 나눠서
        long totalEncryptRsaTime = 0;
        long totalDecryptRsaTime = 0;
        int totalEncryptedRsaLength = 0;
        boolean allRsaMatched = true;

        for (int i = 0; i < REPEAT; i++) {
            long start = System.nanoTime();
            byte[] part1 = Arrays.copyOfRange(message, 0, 150);
            byte[] part2 = Arrays.copyOfRange(message, 150, message.length);

            byte[] encrypted1 = encryptWithRSA(rsaPair.getPublic(), part1);
            byte[] encrypted2 = encryptWithRSA(rsaPair.getPublic(), part2);
            long encryptRsaTime = System.nanoTime() - start;

            start = System.nanoTime();
            byte[] decrypted1 = decryptWithRSA(rsaPair.getPrivate(), encrypted1);
            byte[] decrypted2 = decryptWithRSA(rsaPair.getPrivate(), encrypted2);

            byte[] combinedDecrypted = new byte[decrypted1.length + decrypted2.length];
            System.arraycopy(decrypted1, 0, combinedDecrypted, 0, decrypted1.length);
            System.arraycopy(decrypted2, 0, combinedDecrypted, decrypted1.length, decrypted2.length);
            long decryptRsaTime = System.nanoTime() - start;

            if (!Arrays.equals(message, combinedDecrypted))
                allRsaMatched = false;

            totalEncryptRsaTime += encryptRsaTime;
            totalDecryptRsaTime += decryptRsaTime;
            totalEncryptedRsaLength += (encrypted1.length + encrypted2.length);
        }

        System.out.printf("Average Encrypted Length: %.2f bytes%n", totalEncryptedRsaLength / (double) REPEAT);
        System.out.printf("Average Encrypt Time: %.6f ms%n", totalEncryptRsaTime / (REPEAT * 1_000_000.0));
        System.out.printf("Average Decrypt Time: %.6f ms%n", totalDecryptRsaTime / (REPEAT * 1_000_000.0));
        System.out.println("All Decrypted == Original: " + allRsaMatched);
    }

    private static byte[] encryptWithECIES(PublicKey publicKey, byte[] plaintext) throws Exception {
        Cipher iesCipher = Cipher.getInstance("ECIES", "BC");
        iesCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return iesCipher.doFinal(plaintext);
    }

    private static byte[] decryptWithECIES(PrivateKey privateKey, byte[] encrypted) throws Exception {
        Cipher iesCipher = Cipher.getInstance("ECIES", "BC");
        iesCipher.init(Cipher.DECRYPT_MODE, privateKey);
        return iesCipher.doFinal(encrypted);
    }

    private static byte[] encryptWithRSA(PublicKey publicKey, byte[] plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plaintext);
    }

    private static byte[] decryptWithRSA(PrivateKey privateKey, byte[] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encrypted);
    }
}
