package src;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Key;
import javax.crypto.KeyGenerator;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;



public class Encryption {

    private static final String encoding = "UTF-8";
    private static final String encryptionAlgorithm = "AES";
    private static final String cipherType = "AES/CBC/PKCS5PADDING";
    private static final int keySize = 128;
    Key encryptionKey;
    //byte[] ivBytes;
    //IvParameterSpec iv;


    public Encryption() {
         
    }

    public Encryption(String keyString) {

        Base64.Decoder decoder = Base64.getDecoder();
        // Base64.Encoder encoder = Base64.getEncoder();

        //byte[] encodedKey = decoder.decode(keyString);

        //System.out.println(ivString);

        try {
            byte[] encodedKey = keyString.getBytes("UTF-8");
            this.encryptionKey = new SecretKeySpec(encodedKey,0,encodedKey.length, encryptionAlgorithm);

            //this.ivBytes = ivString.getBytes("UTF-8");  
            //this.iv = new IvParameterSpec(this.ivBytes);

        } catch (UnsupportedEncodingException e) {
            System.out.println("Encoding Exception");
        }


        // String key = "";
        // String masterPassword = "";
        // String iv = "";

        // try {
        //     //File keyFile = new File(fileName);
        //     //Scanner reader = new Scanner(keyFile);

        //     // key = lines.get(0);

        //     // iv = lines.get(1);

        //     // masterPassword = lines.get(2);

        //     // while (reader.hasNextLine()) {
        //     //     key = reader.nextLine();
        //     // }
        //     //reader.close();

        //     this.encryptionKey = key;
        //     byte[] encodedKey = decoder.decodeBuffer(keyString);
        //     this.encryptionKey = new SecretKeySpec(encodedKey,0,encodedKey.length, encryptionAlgorithm);  
        //     this.iv = iv;
        //     this.password = masterPassword;

        // } catch (FileNotFoundException e) {
        //     System.out.println("File not found.");

        //     this.encryptionKey = generateKey();
        //     this.iv = new IvParameterSpec(generateIv());

        // }
    }

    public String encrypt(String text) {
        String encryptedText = "";

        try {
            SecureRandom random = new SecureRandom();
            byte[] bytes = new byte[16];
            random.nextBytes(bytes);
            IvParameterSpec ivSpec = new IvParameterSpec(bytes);

            

            Cipher cipher = Cipher.getInstance(cipherType);
            //byte[] key = this.encryptionKey.getBytes(encoding);
            //SecretKeySpec secret = new SecretKeySpec(key, encryptionAlgorithm);
            //IvParameterSpec ivParameterSpec = new IvParameterSpec(key);

            cipher.init(Cipher.ENCRYPT_MODE, this.encryptionKey, ivSpec);

            byte[] cipherText = cipher.doFinal(text.getBytes(encoding));
            Base64.Encoder encoder = Base64.getEncoder();
            
            //encryptedText = encoder.encodeToString(bytes) + " " + encoder.encodeToString(cipherText);
            encryptedText = encoder.encodeToString(bytes) + encoder.encodeToString(cipherText);

        } catch (Exception e) {
            System.err.println("Encrypt error: " + e.getMessage());
        }

        return encryptedText;
    }

    public String decrypt(String text) {
        String decryptedText = "";

        try {
            Base64.Decoder decoder = Base64.getDecoder();

            Cipher cipher = Cipher.getInstance(cipherType);

            //Way to decode if using space to seperate iv and encrypted password
            //String[] splitted = text.split(" ");
            //byte[] iv = decoder.decode(splitted[0]);
            //byte[] cipherText = decoder.decode(splitted[1]);

            // System.out.println(text.substring(0,24));
            // System.out.println(text.substring(24));

            byte[] iv = decoder.decode(text.substring(0,24));
            byte[] cipherText = decoder.decode(text.substring(24));

            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.DECRYPT_MODE, this.encryptionKey, ivSpec);
            
            //byte[] cipherText = decoder.decode(text.getBytes(encoding));
            decryptedText = new String(cipher.doFinal(cipherText), encoding);

        } catch (Exception e) {
            System.err.println("Decrypt error: " + e.getMessage());
        }

        return decryptedText;
    }

    // public byte[] generateIv() {
    //     byte[] iv = new byte[16];
    //     Random random = new SecureRandom();
    //     random.nextBytes(iv);
    //     // this.ivBytes = iv;
    //     // this.iv = new IvParameterSpec(iv);
    //     return iv;
    // }

    public Key generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(encryptionAlgorithm);
        keyGen.init(keySize);
        Key key = keyGen.generateKey();
        this.encryptionKey = key;
        return key;
    }
}