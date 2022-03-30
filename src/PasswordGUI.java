package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map.Entry;

public class PasswordGUI implements ActionListener {

    Encryption e;
    FileIO io;
    JFrame mainFrame;
    JFrame passFrame;
    JTextField accountField;
    JTextField userField;
    JTextField passField;
    JTable table;
    JPanel panel;
    //JList<String> usernameList;
    //JList<String> passwordList;
    String passwordFile;
    String keyFile;
    HashMap<String, Entry<String, String>> encryptedPasswords;
    HashMap<String, Entry<String, String>> decryptedPasswords;

    public PasswordGUI(String passwordFile, String keyFile) {
        this.passwordFile = passwordFile;
        this.keyFile = keyFile;

        this.e = new Encryption(keyFile);
        this.io = new FileIO(passwordFile);

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

        this.mainFrame.setSize(700, 600);
    }

    public void display() {
        this.mainFrame.setVisible(true);
        this.mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void createJTable() {
        this.encryptedPasswords = io.readPasswords(this.passwordFile);

        if (!this.encryptedPasswords.isEmpty()) {
            int size = encryptedPasswords.size();

            String[] columns = { "Account", "Username", "Password"};
            String[][] data = getTableData(this.encryptedPasswords);

            System.out.println("Create");
            for (int x = 0; x < data.length; x++) {
                System.out.println(data[x][1]);
            }
            System.out.println();
 
            this.table = new JTable(data, columns);
        }
    }

    public String[][] getTableData(HashMap<String, Entry<String, String>> hashMap) {
        String[][] tempData = new String[hashMap.size()][3];

        int i = 0;
        for (String key : hashMap.keySet()) {
            tempData[i][0] = key;
            tempData[i][1] = hashMap.get(key).getKey();
            tempData[i][2] = hashMap.get(key).getValue();
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
        }
    }

    public void addPasswords() {
        
        if (!this.userField.getText().isEmpty()) {
            String tempAccount = e.encrypt(this.accountField.getText());
            Entry<String, String> tempEntry = new AbstractMap.SimpleEntry<String, String>(e.encrypt(this.userField.getText()), e.encrypt(this.passField.getText()));

            this.encryptedPasswords.put(tempAccount, tempEntry);
            io.writePassword(this.passwordFile, tempAccount, tempEntry);

            createJTable();
            this.table.revalidate();
            this.table.repaint();
            this.panel.revalidate();
            this.panel.repaint();
            //formatGUI();
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

}