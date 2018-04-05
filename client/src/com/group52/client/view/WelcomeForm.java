package com.group52.client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.net.URL;

/**
 * class WelcomeForm is class for create welcome form
 */
public class WelcomeForm extends MainPanel implements Listenable, Closeable {
    public JButton signUpButton = new JButton("sign up");
    public JButton signInButton = new JButton("sign in");
    public JButton ipButton = new JButton("Change server IP address");
    private JFrame frame;

    /**
     * creating welcome form constructor
     * @see WelcomeForm
     */
    public WelcomeForm() {
        frame = new JFrame("Welcome form");
        frame.setSize(300, 260);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        URL iconPath = getClass().getClassLoader().getResource("icon.png");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(iconPath));
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JLabel img;
        URL imgPath = getClass().getClassLoader().getResource("welcome.png");
        if (imgPath != null) {
            img = new JLabel(new ImageIcon(imgPath));
            img.setBounds(10, 10, 280, 110);
            frame.add(img);
        }

        signUpButton.setBounds(20,120,120,40);
        signInButton.setBounds(160,120,120,40);
        ipButton.setBounds(20,170,260,40);
        frame.add(signUpButton);
        frame.add(signInButton);
        frame.add(ipButton);
        frame.setVisible(true);
    }

    @Override
    public void close() {
        frame.setVisible(false);
    }

    @Override
    public void open() {
        frame.setVisible(true);
    }

    @Override
    public void addListener(ActionListener actionListener) {
        signInButton.addActionListener(actionListener);
        signUpButton.addActionListener(actionListener);
        ipButton.addActionListener(actionListener);
    }

    @Override
    public synchronized void addWindowListener(WindowListener l) {
        frame.addWindowListener(l);
    }
}
