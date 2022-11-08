package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowEvent;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map.Entry;

public class PasswordGUI implements ActionListener {

    Encryption e;
    FileIO io;
    JFrame mainFrame;
    JFrame passFrame;
    JFrame masterFrame;
    JTextField accountField;
    JTextField userField;
    JTextField passField;
    JTextField masterField;
    JTable table;
    JPanel panel;
    //JList<String> usernameList;
    //JList<String> passwordList;
    String passwordFile;
    String keyFile;
    Boolean decryptedFlag;
    HashMap<String, Entry<String, String>> encryptedPasswords;
    HashMap<String, Entry<String, String>> decryptedPasswords;

    public PasswordGUI(String passwordFile, String keyFile) {
        this.passwordFile = passwordFile;
        this.keyFile = keyFile;

        this.io = new FileIO(passwordFile, keyFile);
        
        //this.e = this.io.encryption;
        this.e = new Encryption(this.io.getKey());

        formatGUI();
        this.decryptedPasswords = new HashMap<String, Entry<String, String>>();
    }

    public void formatGUI() {
        this.mainFrame = new JFrame("Password Manager");
        this.mainFrame.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        createJTable();

        this.panel = new JPanel();
        this.panel.setSize(new Dimension(500,500));

        JScrollPane scrollPane = new JScrollPane(this.table);
        scrollPane.setPreferredSize(new Dimension(600, 500));

        this.panel.add(scrollPane, SwingConstants.CENTER);

        c.gridx = 0;
        c.gridy = 0;

        this.mainFrame.getContentPane().add(panel);

        JButton addPassword = new JButton("Add Password");

        c.gridx = 0;
        c.gridy = 1;
        this.mainFrame.add(addPassword, c);

        addPassword.addActionListener(this);
        addPassword.setActionCommand("Add");

        JButton decrypt = new JButton("Decrypt/Encrypt");

        c.gridx = 0;
        c.gridy = 2;
        this.mainFrame.add(decrypt, c);

        decrypt.addActionListener(this);
        decrypt.setActionCommand("Decrypt/Encrypt");

        this.mainFrame.setSize(800, 700);
    }

    public void checkPassword() {
        this.masterFrame = new JFrame("Enter Master Password");

        this.masterFrame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = SwingConstants.VERTICAL;

        c.gridx = 0;
        c.gridy = 1;
        JLabel passLabel = new JLabel("Password");
        this.masterFrame.add(passLabel, c);

        c.gridx = 1;
        c.gridy = 1;
        this.masterField = new JTextField();
        this.masterFrame.add(this.masterField, c);

        JButton enter = new JButton("Enter");
        c.gridx = 1;
        c.gridy = 2;
        this.masterFrame.add(enter, c);

        enter.addActionListener(this);
        enter.setActionCommand("master");

        this.masterFrame.setSize(300, 300);
        this.masterFrame.setVisible(true);
        this.masterFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void display() {
        this.mainFrame.setVisible(true);
        this.mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void updateJTable() {

        if (this.decryptedFlag) {
            this.encryptedPasswords = io.readPasswords(this.passwordFile);

            if (!this.encryptedPasswords.isEmpty()) {
    
                String[] columns = { "Account", "Username", "Password"};
                String[][] data = getTableData(this.encryptedPasswords);
    
                System.out.println("Create");
                for (int x = 0; x < data.length; x++) {
                    System.out.println(data[x][1]);
                }
                System.out.println();

                this.decryptedFlag = false;

                DefaultTableModel model = new DefaultTableModel(data, columns);
     
                this.table.setModel(model);
            }
        } else {
            //System.out.println("Decrypt");
            decryptAllPasswords();

            if (!this.decryptedPasswords.isEmpty()) {
                String[] columns = { "Account", "Username", "Password"};
                String[][] data = getTableData(this.decryptedPasswords);

                for (int x = 0; x < data.length; x++) {
                    System.out.println(data[x][0]);
                    System.out.print(data[x][1]);
                    System.out.print(data[x][2]);
                }
                System.out.println();

                this.decryptedFlag = true;

                DefaultTableModel model = new DefaultTableModel(data, columns);

                this.table.setModel(model);
            }

        }
    }

    public void createJTable() {

        this.encryptedPasswords = io.readPasswords(this.passwordFile);

        this.decryptedFlag = false;

        if (!this.encryptedPasswords.isEmpty()) {

            String[] columns = { "Account", "Username", "Password"};
            String[][] data = getTableData(this.encryptedPasswords);

            System.out.println("Create");
            for (int x = 0; x < data.length; x++) {
                System.out.println(data[x][1]);
            }
            System.out.println();

            DefaultTableModel model = new DefaultTableModel(data, columns);
 
            this.table = new JTable(model);
        } else {
            String[] columns = { "Account", "Username", "Password"};
            String[][] data = new String[1][3];

            data[0][0] = "Empty Password File";
            data[0][1] = "Empty Password File";
            data[0][2] = "Empty Password File";

            DefaultTableModel model = new DefaultTableModel(data, columns);
            this.table = new JTable(model);
        }
    }

    public String[][] getTableData(HashMap<String, Entry<String, String>> hashMap) {
        String[][] tempData = new String[hashMap.size()][3];

        int i = 0;
        for (String key : hashMap.keySet()) {
            if (!this.decryptedFlag) {
                tempData[i][0] = key;
                tempData[i][1] = hashMap.get(key).getKey();
                tempData[i][2] = hashMap.get(key).getValue();
            } else {
                tempData[i][0] = key.substring(24);
                tempData[i][1] = hashMap.get(key).getKey().substring(24);
                tempData[i][2] = hashMap.get(key).getValue().substring(24);
            }
            i++;
        }

        return tempData;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Add")) {
            passwordFrame();
        } else if (e.getActionCommand().equals("Enter")) {
            addPasswords();
            this.passFrame.setVisible(false);
            this.passFrame.dispose();
        } else if (e.getActionCommand().equals("Decrypt/Encrypt")) {

            if (!decryptedFlag) {
                checkPassword();
            } else {
                updateJTable();
            }
        } else if (e.getActionCommand().equals("master")) {


            System.out.println(this.masterField.getText());


            /**
             * 
             * INSTEAD ADD THIS FUNCTIONALITY TO DECRYPT NOT TO START OF PROGRAM OR BOTH
             * 
             */



            if (this.masterField.getText().equals("asd")) {

                this.masterFrame.setVisible(false);
                this.masterFrame.dispose();

                decryptAllPasswords();
                updateJTable();

            } else {
                System.out.println("Wrong master password, exiting....");
                this.masterFrame.setVisible(false);
                this.masterFrame.dispose();
                this.mainFrame.dispatchEvent(new WindowEvent(this.mainFrame, WindowEvent.WINDOW_CLOSING));
            }
        }
    }

    public void addPasswords() {
        
        if (!this.userField.getText().isEmpty()) {
            String tempAccount = e.encrypt(this.accountField.getText());
            Entry<String, String> tempEntry = new AbstractMap.SimpleEntry<String, String>(e.encrypt(this.userField.getText()), e.encrypt(this.passField.getText()));

            if (this.decryptedFlag) {
                this.decryptedFlag = false;
            } else {
                this.decryptedFlag = true;
            }

            this.encryptedPasswords.put(tempAccount, tempEntry);
            this.decryptedPasswords.put(this.accountField.getText(), new AbstractMap.SimpleEntry<String, String>(this.userField.getText(), this.passField.getText()));

            // Need to add if statement to check if password already exists, probably in io.writePassword
            io.writePassword(this.passwordFile, tempAccount, tempEntry);

            updateJTable();
        }
    }

    public void passwordFrame() {
        this.passFrame = new JFrame("Add Passwords");

        this.passFrame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = SwingConstants.VERTICAL;

        c.gridx = 0;
        c.gridy = 0;
        JLabel account = new JLabel("Account");
        this.passFrame.add(account, c);

        c.gridx = 1;
        c.gridy = 0;
        this.accountField = new JTextField();
        this.passFrame.add(this.accountField, c);

        c.gridx = 0;
        c.gridy = 2;
        JLabel userLabel = new JLabel("Username");
        this.passFrame.add(userLabel, c);

        c.gridx = 1;
        c.gridy = 2;
        this.userField = new JTextField();
        this.passFrame.add(this.userField, c);

        c.gridx = 0;
        c.gridy = 3;
        JLabel passLabel = new JLabel("Password");
        this.passFrame.add(passLabel, c);

        c.gridx = 1;
        c.gridy = 3;
        this.passField = new JTextField();
        this.passFrame.add(this.passField, c);

        JButton enter = new JButton("Enter");
        c.gridx = 1;
        c.gridy = 4;
        this.passFrame.add(enter, c);

        enter.addActionListener(this);
        enter.setActionCommand("Enter");

        this.passFrame.setSize(200, 200);
        this.passFrame.setVisible(true);
    }

    public void decryptAllPasswords() {

        for (Entry<String,Entry<String,String>> entry : this.encryptedPasswords.entrySet()) {
            String account = this.e.decrypt(entry.getKey());

            Entry<String, String> encryptedEntry = entry.getValue();

            // System.out.println(account);
            // System.out.println(e.decrypt(encryptedEntry.getKey()));
            // System.out.println(e.decrypt(encryptedEntry.getValue()));

            Entry<String, String> decryptedEntry = new AbstractMap.SimpleEntry<String, String>(e.decrypt(encryptedEntry.getKey()), e.decrypt(encryptedEntry.getValue()));
            
            if (!this.decryptedPasswords.containsKey(account)) {
                this.decryptedPasswords.put(account, decryptedEntry);
            }
        }

    }

}