package com.group52.client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

public class IPAddress implements Closeable, Listenable {
    public JButton okButton = new JButton("Ok");
    public JButton quitButton = new JButton("Quit");
    private JTextField iPField = new JTextField(20);
    private JFrame frame;
    private String ip;

    public IPAddress() {
        frame = new JFrame("Task Manager");
        frame.setSize(300, 160);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        URL iconPath = getClass().getClassLoader().getResource("icon.png");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(iconPath));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Enter IP Address of the Server:");
        label.setBounds(20,10,280,40);
        label.setIcon(UIManager.getIcon("OptionPane.questionIcon"));
        label.setFont(new Font("Verdana", Font.PLAIN, 14));

        iPField.setBounds(20,50,260,30);
        okButton.setBounds(110,90,80,30);
        quitButton.setBounds(200,90,80,30);
        frame.add(label);
        frame.add(iPField);
        frame.add(okButton);
        frame.add(quitButton);
    }

    /**
     * method where we read server IP from field
     * @throws NullPointerException if IP field is empty
     */
    public void readServerIP() throws NullPointerException {
        if (iPField.getText().isEmpty()) throw new NullPointerException("Title field is empty");
        ip = iPField.getText();
    }

    /**
     * method where we get server IP
     * @return IP
     */
    public String getIp() {
        return ip;
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
        okButton.addActionListener(actionListener);
        quitButton.addActionListener(actionListener);
    }
}
