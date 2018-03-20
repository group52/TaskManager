package com.group52.client.view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class SignUpForm extends MainPanel implements Listenable, Closeable {

    private JFrame userForm;
    private JTextField openPasswordField;
    private JTextField repeatPasswordField;

    public SignUpForm() {
        userForm = new JFrame("Sign up");
        userForm.setSize(350,450);
        userForm.setLocationRelativeTo(null);
        userForm.setResizable(false);

        userForm.add(loginLabel);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(140,55,280,15);
        userForm.add(passwordLabel);

        userForm.add(loginField);
        openPasswordField = new JTextField(20);
        openPasswordField.setBounds(30,70,280,30);
        userForm.add(openPasswordField);

        repeatPasswordField = new JTextField(20);
        JLabel repeatPasswordLabel = new JLabel("Repeat password");
        repeatPasswordLabel.setBounds(130,105,280,15);
        repeatPasswordField.setBounds(30,120,280,30);
        userForm.add(repeatPasswordLabel);
        userForm.add(repeatPasswordField);

        userForm.add(confirmButton);
        userForm.add(cancelButton);

        userForm.setLayout(null);
    }

    public String getLogin() {
        return loginField.getText();
    }

    public String getPassword() {
        return openPasswordField.getText();
    }

    public String getRepeatedPassword() {
        return repeatPasswordField.getText();
    }

    public void open() {
        userForm.setVisible(true);
    }

    public void close() {
        userForm.setVisible(false);
    }

    public void addListener(ActionListener actionListener) {
        confirmButton.addActionListener(actionListener);
        cancelButton.addActionListener(actionListener);
    }
}
