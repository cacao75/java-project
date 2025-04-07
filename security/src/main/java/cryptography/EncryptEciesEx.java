package cryptography;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Arrays;
//import java.util.Random;

public class EncryptEciesEx {

//    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECDH", BouncyCastleProvider.PROVIDER_NAME);
        kpg.initialize(new ECGenParameterSpec("prime256v1"));

        KeyPair keyPair = kpg.generateKeyPair();
        PublicKey ssKey = keyPair.getPublic();
        PrivateKey skKey = keyPair.getPrivate();

        // example 93 bytes as maximum expected payload
//        byte[] message = new byte[93];
//        RANDOM.nextBytes(message);
        String myString = "Insanity is doing the same thing over and over again and expecting different results. - R.M.Brown";
        System.out.println("Message: " + myString);
        byte[] message = myString.getBytes(StandardCharsets.UTF_8);
        print("Plaintext", message);

        //encrypt
        byte[] encrypted = encrypt(ssKey, message);
        print("Encrypted", encrypted);

        //decrypt
        byte[] decrypted = decrypt(skKey, encrypted);
        print("Decrypted", decrypted);

        if(Arrays.equals(message, decrypted))
            System.out.println("SUCCESS - Decrypted == Message");
        else
            System.out.println("FAIL - Decrypted != Message");

        String yrString = new String(decrypted, StandardCharsets.UTF_8);
        System.out.println("Message: " + yrString);
    }

    private static byte[] encrypt(PublicKey publicKey, byte[] plaintext) throws Exception {
        Cipher iesCipher = Cipher.getInstance("ECIES", BouncyCastleProvider.PROVIDER_NAME);
        iesCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return iesCipher.doFinal(plaintext);
    }

    private static byte[] decrypt(PrivateKey privateKey, byte[] encrypted) throws Exception {
        Cipher iesCipher2 = Cipher.getInstance("ECIES", BouncyCastleProvider.PROVIDER_NAME);
        iesCipher2.init(Cipher.DECRYPT_MODE, privateKey);
        return iesCipher2.doFinal(encrypted);
    }

    private static void print(String prefix, byte[] bytes) {
        System.out.println(prefix + " (len=" + bytes.length + "): " + new String(Hex.encode(bytes)));
    }
}
