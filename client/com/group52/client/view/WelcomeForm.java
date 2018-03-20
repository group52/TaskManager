package com.group52.client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class WelcomeForm implements Listenable, Closeable {
    public JButton signUpButton = new JButton("sign up");
    public JButton signInButton = new JButton("sign in");
    private JFrame frame;

    public WelcomeForm() {
        frame = new JFrame("Welcome form");
        frame.setSize(300, 200);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("img/welcome.png"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        signUpButton.setBounds(40,110,100,40);
        signInButton.setBounds(150,110,100,40);
        frame.add(signUpButton);
        frame.add(signInButton);
        frame.setVisible(true);
    }

    @Override
    public void close() {
        frame.setVisible(false);
    }

    @Override
    public void addListener(ActionListener actionListener) {
        signInButton.addActionListener(actionListener);
        signUpButton.addActionListener(actionListener);
    }
}
