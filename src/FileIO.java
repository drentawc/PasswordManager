package src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map.Entry;

public class FileIO {

    File passwordFile;
    HashMap<String, Entry<String, String>> passwords;
    HashMap<String, Entry<String, String>> newPasswords;

    public FileIO(String fileName) {

        File temp = new File(fileName);

        if (temp.exists()) {

            this.passwordFile = temp;
            this.passwords = readPasswords(fileName);

        } else {
            createFile(fileName);
        }

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

    public void createFile(String fileName) {
        //Try to create new file with fileName if not write to file
        try {
            this.passwordFile = new File(fileName);
            if (!this.passwordFile.createNewFile()) {
                System.out.println("Error creating file");
            }
        } catch (IOException e) {
            System.out.println("Error creating file");
        }
    }

}
