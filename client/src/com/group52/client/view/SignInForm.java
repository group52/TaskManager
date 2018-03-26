package com.group52.client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * class SignInForm is class for create authorization form
 */
public class SignInForm extends MainPanel implements Listenable, Closeable {

    private JFrame authorizationForm;
    private JPasswordField secretPasswordField;

    /**
     * creating authorization form constructor
     * @see SignInForm
     */
    public SignInForm() {
        authorizationForm = new JFrame("Sign in");
        authorizationForm.setSize(350,450);
        authorizationForm.setLocationRelativeTo(null);
        authorizationForm.setResizable(false);
        URL iconPath = getClass().getClassLoader().getResource("icon.png");
        authorizationForm.setIconImage(Toolkit.getDefaultToolkit().getImage(iconPath));

        authorizationForm.add(loginLabel);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(140,55,280,15);
        authorizationForm.add(passwordLabel);

        authorizationForm.add(loginField);
        secretPasswordField = new JPasswordField(20);
        secretPasswordField.setBounds(30,70,280,30);
        authorizationForm.add(secretPasswordField);

        authorizationForm.add(confirmButton);
        authorizationForm.add(cancelButton);

        authorizationForm.setLayout(null);
    }

    /**
     * method where we get login of user
     * @throws NullPointerException if login field is empty
     * @return login
     */
    public String getLogin() throws NullPointerException {
        if (loginField.getText().isEmpty()) throw new NullPointerException("Login field is empty");
        return loginField.getText();
    }

    /**
     * method where we get password of user
     * @throws NullPointerException if password field is empty
     * @return password
     */
    public String getPassword() throws NullPointerException {
        String password = String.valueOf(secretPasswordField.getPassword());
        if (password.isEmpty()) throw new NullPointerException("Password field is empty");
        return password;
    }

    @Override
    public void open() {
        authorizationForm.setVisible(true);
    }

    @Override
    public void close () {
        authorizationForm.setVisible(false);
    }

    @Override
    public void addListener(ActionListener actionListener) {
        confirmButton.addActionListener(actionListener);
        cancelButton.addActionListener(actionListener);
    }

}
