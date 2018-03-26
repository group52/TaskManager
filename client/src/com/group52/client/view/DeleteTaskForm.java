package com.group52.client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * class DeleteTaskForm is class for create delete task form
 */
public class DeleteTaskForm extends MainPanel implements Listenable {

    private JFrame taskForm;
    public JButton deleteTaskButton = new JButton("Delete task");

    /**
     * creating delete task form constructor
     * @see DeleteTaskForm
     */
    public DeleteTaskForm() {
        taskForm = new JFrame("Delete task");
        taskForm.setSize(350,450);
        taskForm.setLocationRelativeTo(null);
        taskForm.setResizable(false);
        URL iconPath = getClass().getClassLoader().getResource("icon.png");
        taskForm.setIconImage(Toolkit.getDefaultToolkit().getImage(iconPath));

        JScrollPane scrollPane = new JScrollPane(comboBox);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(30, 160, 300,100);
        taskForm.getContentPane().add(scrollPane);

        JLabel img;
        URL imgPath = getClass().getClassLoader().getResource("delete.png");
        if (imgPath != null) {
            img = new JLabel(new ImageIcon(imgPath));
            img.setBounds(30, 15, 300,120);
            taskForm.add(img);
        }

        deleteTaskButton.setBounds(30,300,280,40);
        taskForm.add(deleteTaskButton);
        taskForm.add(cancelButton);
        taskForm.setLayout(null);
    }

    @Override
    public void open() {
        taskForm.setVisible(true);
    }

    @Override
    public void close() {
        taskForm.setVisible(false);
    }

    @Override
    public void addListener(ActionListener actionListener) {
        deleteTaskButton.addActionListener(actionListener);
        cancelButton.addActionListener(actionListener);
    }
}
