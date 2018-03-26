package com.group52.client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * class SignUpForm is class for create sign up form
 */
public class SignUpForm extends MainPanel implements Listenable, Closeable {

    private JFrame userForm;
    private JTextField passwordField;
    private JTextField repeatPasswordField;

    /**
     * creating sign up form constructor
     * @see SignUpForm
     */
    public SignUpForm() {
        userForm = new JFrame("Sign up");
        userForm.setSize(350,450);
        userForm.setLocationRelativeTo(null);
        userForm.setResizable(false);
        URL iconPath = getClass().getClassLoader().getResource("icon.png");
        userForm.setIconImage(Toolkit.getDefaultToolkit().getImage(iconPath));

        userForm.add(loginLabel);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(140,55,280,15);
        userForm.add(passwordLabel);

        userForm.add(loginField);
        passwordField = new JTextField(20);
        passwordField.setBounds(30,70,280,30);
        userForm.add(passwordField);

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
    public String getPassword() {
        if (passwordField.getText().isEmpty()) throw new NullPointerException("Password field is empty");
        return passwordField.getText();
    }

    /**
     * method where we get repeated password of user
     * @throws NullPointerException if repeated password field is empty
     * @return repeated password
     */
    public String getRepeatedPassword() {
        if (repeatPasswordField.getText().isEmpty()) throw new NullPointerException("Repeat password field is empty");
        return repeatPasswordField.getText();
    }

    @Override
    public void open() {
        userForm.setVisible(true);
    }

    @Override
    public void close() {
        userForm.setVisible(false);
    }

    @Override
    public void addListener(ActionListener actionListener) {
        confirmButton.addActionListener(actionListener);
        cancelButton.addActionListener(actionListener);
    }
}
