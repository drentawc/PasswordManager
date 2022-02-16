package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import java.util.HashMap;

public class FileIO {

    HashMap<String, String> passwords;
    HashMap<String, String> newPasswords;

    public FileIO(String fileName) {

        passwords = readPasswords(fileName);

    }

    public HashMap<String, String> readPasswords(String fileName) {
        HashMap<String, String> tempPasswords = new HashMap<String, String>();

        try {
            File passwordFile = new File(fileName);
            Scanner reader = new Scanner(passwordFile);

            while (reader.hasNextLine()) {
                
                String[] temp = reader.nextLine().split(" ");

                tempPasswords.put(temp[0], temp[1]);

            }
            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

        return tempPasswords;
    }

    public void writePasswords(String fileName) {

        File temp = new File(fileName);

        if (temp.exists()) {

            try {
                FileWriter writer = new FileWriter(fileName);
                // for (int i = 0; i < encryptedPasswords.length(); i++) {
                //     writer.write(encryptedPasswords[i]);
                // }
                writer.close();
            } catch (IOException e) {
                System.out.println("IOError");
            }

        } else {
            createFile(fileName);
        }
    
    }

    public void createFile(String fileName) {
        //Try to create new file with fileName if not write to file
        try {
            File password = new File(fileName);
            if (password.createNewFile()) {
                System.out.println("File created");
            } else{
                System.out.println("File already exists");
            }
        } catch (IOException e) {
            System.out.println("Error creating file");
        }
    }

}
