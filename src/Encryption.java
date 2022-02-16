package src;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Encryption {

    private static final String encoding = "UTF-8";
    private static final String encryptionAlgorithm = "AES";
    private static final String cipherType = "AES/CBC/PKCS5PADDING";
    private static String encryptionKey;

    public Encryption(String fileName) {

        String key = "";

        try {
            File keyFile = new File(fileName);
            Scanner reader = new Scanner(keyFile);
            while (reader.hasNextLine()) {
                key = reader.nextLine();
            }
            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        this.encryptionKey = key;
    }

    public static String encrypt(String text) {
        String encryptedText = "";

        try {
            Cipher cipher = Cipher.getInstance(cipherType);
            byte[] key = encryptionKey.getBytes(encoding);
            SecretKeySpec secret = new SecretKeySpec(key, encryptionAlgorithm);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(key);
            cipher.init(Cipher.ENCRYPT_MODE, secret, ivParameterSpec);
            byte[] cipherText = cipher.doFinal(text.getBytes(encoding));
            Base64.Encoder encoder = Base64.getEncoder();
            encryptedText = encoder.encodeToString(cipherText);

        } catch (Exception e) {
            System.err.println("Encrypt error: " + e.getMessage());
        }

        return encryptedText;
    }

    public static String decrypt(String text) {
        String decryptedText = "";

        try {
            Cipher cipher = Cipher.getInstance(cipherType);
            byte[] key = encryptionKey.getBytes(encoding);
            SecretKeySpec secretKey = new SecretKeySpec(key, encryptionAlgorithm);
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] cipherText = decoder.decode(text.getBytes(encoding));
            decryptedText = new String(cipher.doFinal(cipherText), encoding);

        } catch (Exception e) {
            System.err.println("Decrypt error: " + e.getMessage());
        }

        return decryptedText;
    }
}