import src.*;

/**
  Password Manager GUI using AES 128 bit encryption.
  @author: Will Drenta
  @version: 1.0 02/14/22
 */
public class PasswordManager {

    public static void main(String[] args) {

        // Encryption e = new Encryption("key.txt");

        //String passwordFile = "passwords.txt";
        String passwordFile = "/Volumes/AES-Key/passwords.txt";
        String keyFile = "key2.txt";

        for (String arg : args) {
            System.out.println(arg);
        }

        //Encryption e = new Encryption();

        // FileIO io = new FileIO(passwordFile, keyFile);
        // Encryption e = new Encryption(io.getKey());

        //Encryption e = new Encryption();

        // String test = "testString123";

        // String encrytpedString1 = e.encrypt(test);

        // String encrytpedString2 = e.encrypt(test);

        // System.out.println(encrytpedString1);

        // System.out.println(encrytpedString1.length());

        // System.out.println(encrytpedString2);

        // System.out.println(encrytpedString2.length());

        // System.out.println(e.decrypt(encrytpedString1));

        // System.out.println(e.decrypt(encrytpedString2));

        //FileGUI filegui = new FileGUI();

        PasswordGUI gui = new PasswordGUI();
        gui.display();
    }
}