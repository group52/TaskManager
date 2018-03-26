package com.group52.client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * class NotificationForm is class for create notification form
 */
public class NotificationForm implements Closeable, Listenable {

    private JFrame notificationForm;
    private JTextArea tasksField = new JTextArea();
    public JButton closeTaskButton = new JButton("Close task");
    public JButton postponeTaskButton = new JButton("Postpone for 5 minutes");

    /**
     * creating notification form constructor
     * @see NotificationForm
     */
    public NotificationForm() {
        notificationForm = new JFrame("Notification");
        notificationForm.setSize(460,190);
        notificationForm.setLocationRelativeTo(null);
        notificationForm.setResizable(false);
        URL iconPath = getClass().getClassLoader().getResource("alarm.png");
        notificationForm.setIconImage(Toolkit.getDefaultToolkit().getImage(iconPath));
        notificationForm.setLayout(null);

        tasksField.setEditable(false);
        tasksField.setBounds(30,10,400,100);
        closeTaskButton.setBounds(30,110, 200,40);
        postponeTaskButton.setBounds(230, 110, 200, 40);
        notificationForm.add(tasksField);
        notificationForm.add(closeTaskButton);
        notificationForm.add(postponeTaskButton);
    }

    /**
     * method for show task
     * @param task is task to show
     */
    public void showTask(String task) {
        tasksField.setText(task);
    }

    @Override
    public void open() {
        notificationForm.setVisible(true);
    }

    @Override
    public void close () {
        notificationForm.setVisible(false);
    }

    @Override
    public void addListener(ActionListener actionListener) {
        closeTaskButton.addActionListener(actionListener);
        postponeTaskButton.addActionListener(actionListener);
    }
}
