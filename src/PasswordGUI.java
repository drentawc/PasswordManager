package src;

import src.Encryption;
import src.FileIO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import java.util.HashMap;

public class PasswordGUI implements ActionListener {

    JFrame mainFrame;
    HashMap<String, String> encryptedPasswords;
    HashMap<String, String> exportPasswords;

    public PasswordGUI() {
        Encryption e = new Encryption("key.txt");

        formatGUI();
    }

    public void formatGUI() {
        mainFrame = new JFrame("Password Manager");
        mainFrame.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        // c.fill = GridBagConstraints.HORIZONTAL;
        
        JScrollPane passwordPane = new JScrollPane();
        passwordPane.setPreferredSize(new Dimension(200,200));

        c.gridx = 0;
        c.gridy = 0;
        
        mainFrame.add(passwordPane, c);

        JButton addPassword = new JButton("Add Password");

        c.gridx = 0;
        c.gridy = 1;
        mainFrame.add(addPassword, c);

        addPassword.addActionListener(this);
        addPassword.setActionCommand("Add");

        mainFrame.setSize(400, 400);
        //mainFrame.setLayout(null);

        //c.fill = GridBagConstraints.HORIZONTAL;

    }

    public void display() {
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void addLabels() {

    }

    public void addPasswords() {

    }

    public void actionPerformed(ActionEvent e) {
        passwordFrame();
    }

    public void passwordFrame() {
        JFrame frame = new JFrame("Add Passwords");

        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = SwingConstants.VERTICAL;

        c.gridx = 0;
        c.gridy = 0;
        JLabel userLabel = new JLabel("Username");
        frame.add(userLabel);

        c.gridx = 1;
        c.gridy = 0;
        JTextField userField = new JTextField();
        frame.add(userField);

        c.gridx = 0;
        c.gridy = 2;
        JLabel passLabel = new JLabel("Password");
        frame.add(passLabel);

        c.gridx = 1;
        c.gridy = 2;
        JTextField passField = new JTextField();
        frame.add(passField);

        frame.setSize(400, 400);
        frame.setVisible(true);

    }

}