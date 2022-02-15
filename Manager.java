import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**

  Password Manager GUI using AES 128 bit encryption.
  @author: Will Drenta
  @version: 1.0 02/14/22
 */
public class Main {

    public static void main(String[] args) {

        Encryption encryption = new Encryption("key.txt");

        //PasswordGUI gui = new PasswordGUI();

    }

}

public class Encryption {

    private static final String encoding = "UTF-8";
    private static final String enscryptionAlgorithm = "AES";
    private static final String cipherType = "AES/CBC/PKCS5PADDING";

    public Encryption(String filePath) {

        String key = "";

        try {
            File keyFile = new File(filePath);
            Scanner reader = new Scanner(keyFile);
            while (reader.hasNextLine()) {
                key = reader.nextLine();
            }
            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        String encryptionKey = key;
    }

    public static String encrypt(String text) {
        String encryptedText = "";

        try {
            Ciper cipher = Cipher.getInstance(cipherType);
            byte[] key = encryptionKey.getBytes(encoding);
            SecretKeySpec secret = new SecretKeySpec(key, enscryptionAlgorithm);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(key);
            cipher.init(Cipher.ENCRPYT_MODE, secret, ivParameterSpec);
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
            SecretKeySpec secretKey = new SecretKeySpec(key, enscryptionAlgorithm);
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] cipherText = decoder.decode(encryptedText.getBytes(encoding));
            decryptedText = new String(cipher.doFinal(cipherText), encoding);

        } catch (Exception e) {
            System.err.println("Decrypt error: " + e.getMessage());
        }

        return decryptedText;
    }
}

public class PasswordGUI {

    JFrame mainFrame;

    public PasswordGUI() {
        mainFrame = new JFrame();


        JScrollPane passwordPane = new JScrollPane();
        passwordPane.setBounds(100, 100, 200, 200);

        mainFrame.add(passwordPane);
        mainFrame.setSize(400, 400);
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}