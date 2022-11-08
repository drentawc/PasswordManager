package src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.nio.file.Files;

import java.util.Scanner;
import java.util.AbstractMap.SimpleEntry;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.List;

public class FileIO {

    File passwordFile;
    File keyFile;
    Encryption encrypt;
    String key;
    String masterPassword;
    String iv;
    HashMap<String, Entry<String, String>> passwords;
    HashMap<String, Entry<String, String>> newPasswords;

    public FileIO(String passwordFileName, String keyFileName) {

        File temp = new File(passwordFileName);
        if (temp.exists()) {

            this.passwordFile = temp;
            this.passwords = readPasswords(passwordFileName);

        } else {
            createFile(passwordFileName);
        }

        File tempKey = new File(keyFileName);
        if (tempKey.exists()) {

            this.keyFile = tempKey;
            String[] keys = readKey(keyFileName);

            this.key = keys[0];
            this.masterPassword = keys[1];
            this.iv = keys[2];

        } else {
            createFile(keyFileName);
        }
    }

    public String[] readKey(String fileName) {

        String key = "";
        String iv = "";
        String masterPassword = "";

        try {

            List<String> lines = Files.readAllLines(Paths.get(fileName));

            key = lines.get(0);
            iv = lines.get(1);
            masterPassword = lines.get(2);

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
            System.out.println("IOException");
        }
        return new String[]{ key, masterPassword, iv };  
    }

    public HashMap<String, Entry<String, String>> readPasswords(String fileName) {
        HashMap<String, Entry<String, String> > tempPasswords = new HashMap<String, Entry<String, String>>();

        try {
            //File passwordFile = new File(fileName);
            Scanner reader = new Scanner(this.passwordFile);

            while (reader.hasNextLine()) {
                
                String[] temp = reader.nextLine().split(" ");

                tempPasswords.put(temp[0], new SimpleEntry<String, String>(temp[1], temp[2]));

            }
            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("");
        }

        return tempPasswords;
    }

    //Maybe have a method to write one single password/boolean to specify 

    public void writePassword(String fileName, String account, Entry<String, String> entry) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            Scanner reader = new Scanner(this.passwordFile);
            String line = account + " " + entry.getKey() + " " + entry.getValue();

            if (this.passwordFile.length() == 0) {
                System.out.println("File empty");
                writer.write(line + "\n");
            } else {
                while (reader.hasNextLine()) {
                    String tempLine = reader.nextLine();
                    
                    if (!tempLine.equals(line)) {
                        //Change to append
                        writer.write(line + "\n");
                    } else {
                        System.out.println("Password exists");
                    }
                }
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("IOError");
        }
    }

    public void writePasswords(String fileName, HashMap<String,String> passwords) {

        try {

            FileWriter writer = new FileWriter(fileName);
            // for (int i = 0; i < encryptedPasswords.length(); i++) {
            //     writer.write(encryptedPasswords[i]);
            // }
            writer.close();
        } catch (IOException e) {
            System.out.println("IOError");
        }
    
    }

    public void writeKeyFile(File keyFile) {

        Encryption encrypt = new Encryption();

        try {
            FileWriter writer = new FileWriter(keyFile);

            //FileOutputStream fout = new FileOutputStream(keyFile);

            Scanner input = new Scanner(System.in);
            System.out.println("Please enter master password: ");
            String password = input.nextLine();

            byte[] key = encrypt.generateKey().getEncoded();

            String keyString = Base64.getEncoder().encodeToString(key) + "\n";
            String ivString = Base64.getEncoder().encodeToString(encrypt.generateIv()) + "\n";
            String encryptedPassword = encrypt.encrypt(password);
            

            writer.write(keyString);
            writer.write(ivString);
            writer.write(encryptedPassword);


            this.key = keyString;
            this.masterPassword = ivString;
            this.iv = encryptedPassword;

            writer.close();
            input.close();
        } catch (IOException e) {
            System.out.println("IOError");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException");
        }
    }

    public void createFile(String fileName) {
        //Try to create new file with fileName if not write to file
        try {

            //Add if to check if key is in file name and if it is create that, then prompt user for key and master password, key in line 1, encrypted master password in line 2

            if (fileName.contains("key")) {
                this.keyFile = new File(fileName);
                if (!this.keyFile.createNewFile()) {
                    System.out.println("Error creating file");
                } else {
                    writeKeyFile(this.keyFile);
                }
            } else {
                this.passwordFile = new File(fileName);
                if (!this.passwordFile.createNewFile()) {
                    System.out.println("Error creating file");
                }
            }
        } catch (IOException e) {
            System.out.println("Error creating file");
        }
    }

    public String getKey() {
        return this.key;
    }

    public String getPassword() {
        return this.masterPassword;
    }

    public String getIv() {
        return this.iv;
    }

}
