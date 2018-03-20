package com.group52.client.view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class SignInForm extends MainPanel implements Listenable, Closeable {

    private JFrame authorizationForm;
    private JPasswordField closePasswordField;

    public SignInForm() {

        authorizationForm = new JFrame("Sign in");
        authorizationForm.setSize(350,450);
        authorizationForm.setLocationRelativeTo(null);
        authorizationForm.setResizable(false);

        authorizationForm.add(loginLabel);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(140,55,280,15);
        authorizationForm.add(passwordLabel);

        authorizationForm.add(loginField);
        closePasswordField = new JPasswordField(20);
        closePasswordField.setBounds(30,70,280,30);
        authorizationForm.add(closePasswordField);

        authorizationForm.add(confirmButton);
        authorizationForm.add(cancelButton);

        authorizationForm.setLayout(null);
    }

    public String getLogin() {
        return loginField.getText();
    }

    public String getPassword() {
        return String.valueOf(closePasswordField.getPassword());
    }

    public void open() {
        authorizationForm.setVisible(true);
    }

    public void close () {
        authorizationForm.setVisible(false);
    }

    public void addListener(ActionListener actionListener) {
        confirmButton.addActionListener(actionListener);
        cancelButton.addActionListener(actionListener);
    }

}
