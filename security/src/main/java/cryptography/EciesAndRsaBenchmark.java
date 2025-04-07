package cryptography;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Arrays;

public class EciesAndRsaBenchmark {

    private static final int REPEAT = 100;

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        // 97 bytes
        String messageStr = "Insanity is doing the same thing over and over again and expecting different results. - R.M.Brown";
//        String messageStr = """
//                The cosmos is within us.
//                We are made of star-stuff.
//                We are a way for the universe to know itself.
//                Somewhere, something incredible is waiting to be known.
//                We are the legacy of 15 billion years of cosmic evolution.
//                """;
        byte[] message = messageStr.getBytes(StandardCharsets.UTF_8);

        System.out.println("Benchmark message: " + messageStr);
        System.out.println("Length: " + message.length + " bytes\n");

        benchmarkECIES(message);
        benchmarkRSA(message);
    }

    private static void benchmarkECIES(byte[] message) throws Exception {
        System.out.println("=== ECIES ===");
        KeyPairGenerator ecKpg = KeyPairGenerator.getInstance("ECDH", BouncyCastleProvider.PROVIDER_NAME);
        ecKpg.initialize(new ECGenParameterSpec("prime256v1"));
        KeyPair ecKeyPair = ecKpg.generateKeyPair();

        long encTotal = 0, decTotal = 0;
        byte[] lastEncrypted = null;

        for (int i=0; i < REPEAT; i++) {
            long t1 = System.nanoTime();
            byte[] encrypted = eciesEncrypt(ecKeyPair.getPublic(), message);
            long t2 = System.nanoTime();
            byte[] decrypted = eciesDecrypt(ecKeyPair.getPrivate(), encrypted);
            long t3 = System.nanoTime();

            if (!Arrays.equals(message, decrypted)) {
                throw new RuntimeException("ECIES Decryption failed!");
            }

            encTotal += (t2 - t1);
            decTotal += (t3 - t2);
            lastEncrypted = encrypted;
        }

        System.out.println("Average Encrypt Time: " + (encTotal / REPEAT) / 1_000_000.0 + " ms");
        System.out.println("Average Decrypt Time: " + (decTotal / REPEAT) / 1_000_000.0 + " ms");
        System.out.println("Encrypted length: " + lastEncrypted.length + " bytes\n");
    }

    private static byte[] eciesEncrypt(PublicKey key, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("ECIES", BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    private static byte[] eciesDecrypt(PrivateKey key, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("ECIES", BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    private static void benchmarkRSA(byte[] message) throws Exception {
        System.out.println("=== RSA ===");
        KeyPairGenerator rsaKpg = KeyPairGenerator.getInstance("RSA");
        rsaKpg.initialize(2048);
        KeyPair rsaKeyPair = rsaKpg.generateKeyPair();

        long encTotal = 0, decTotal = 0;
        byte[] lastEncrypted = null;

        for (int i = 0; i < REPEAT; i++) {
            long t1 = System.nanoTime();
            byte[] encrypted = rsaEncrypt(rsaKeyPair.getPublic(), message);
            long t2 = System.nanoTime();
            byte[] decrypted = rsaDecrypt(rsaKeyPair.getPrivate(), encrypted);
            long t3 = System.nanoTime();

            if (!Arrays.equals(message, decrypted)) {
                throw new RuntimeException("RSA Decryption failed!");
            }

            encTotal += (t2 - t1);
            decTotal += (t3 - t2);
            lastEncrypted = encrypted;
        }

        System.out.println("Average Encrypt Time: " + (encTotal / REPEAT) / 1_000_000.0 + " ms");
        System.out.println("Average Decrypt Time: " + (decTotal / REPEAT) / 1_000_000.0 + " ms");
        System.out.println("Encrypted length: " + lastEncrypted.length + " bytes\n");
    }

    private static byte[] rsaEncrypt(PublicKey key, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    private static byte[] rsaDecrypt(PrivateKey key, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }
}
