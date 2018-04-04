package com.group52.client.view;

import org.jdatepicker.impl.JDatePickerImpl;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

/**
 * class MainPanel is class for create main panel, common fields and supply methods
 */
public class MainPanel extends JFrame implements Listenable, Closeable {

    private JTextArea tasksField = new JTextArea();

    protected JTextField titleField = new JTextField(20);
    protected JTextField descriptionField = new JTextField(20);
    protected JTextField loginField = new JTextField(20);
    protected JTextField intervalField = new JTextField(20);

    protected JLabel titleLabel = new JLabel("Title");
    protected JLabel descriptionLabel = new JLabel("Description");
    protected JLabel loginLabel = new JLabel("Login");
    protected JLabel startDateLabel = new JLabel("Start date");
    protected JLabel endDateLabel = new JLabel("End date");
    protected JLabel startHoursLabel = new JLabel("Hour");
    protected JLabel endHoursLabel = new JLabel("Hour");
    protected JLabel startMinutesLabel = new JLabel("Minute");
    protected JLabel endMinutesLabel = new JLabel("Minute");
    protected JLabel intervalLabel = new JLabel("Interval(seconds)");

    public JButton confirmButton = new JButton("Confirm");
    public JButton unrepeatableTaskFormButton = new JButton("Create task");
    public JButton repeatableTaskFormButton = new JButton("Create repeatable task");
    public JButton editTaskFormButton = new JButton("Edit Task");
    public JButton deleteTaskFormButton = new JButton("Delete Task");
    public JButton calendarFormButton = new JButton("Show calendar");

    public JButton cancelButton = new JButton("Cancel");
    public JButton exitButton = new JButton("Logout");

    public JCheckBox activeBox = new JCheckBox("is Active");
    public JComboBox comboBox = new JComboBox();

    protected JSpinner startHoursSpinner = new JSpinner(new SpinnerNumberModel(8, 1, 24, 1));
    protected JSpinner endHoursSpinner = new JSpinner(new SpinnerNumberModel(8, 1, 24, 1));
    protected JSpinner startMinutesSpinner = new JSpinner(new SpinnerNumberModel(8, 0, 60, 1));
    protected JSpinner endMinutesSpinner = new JSpinner(new SpinnerNumberModel(8, 0, 60, 1));

    /**
     * empty constructor for create main panel
     * @see MainPanel
     */
    public MainPanel() {
        JPanel mainPanel = new JPanel();
        this.setTitle("Task Manager");
        this.setSize(810,450);
        URL iconPath = getClass().getClassLoader().getResource("icon.png");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(iconPath));
        tasksField.setEditable(false);

        JScrollPane areaScrollPane = new JScrollPane(tasksField);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        areaScrollPane.setBounds(30,10, 740, 280);
        getContentPane().add(areaScrollPane);

        titleLabel.setBounds(150,5,280,15);
        descriptionLabel.setBounds(150,55,280,15);
        loginLabel.setBounds(150,5,280,15);
        startDateLabel.setBounds(30,105,140,15);
        endDateLabel.setBounds(30,155,140,15);
        startHoursLabel.setBounds(180,105,60,15);
        endHoursLabel.setBounds(180,155,60,15);
        startMinutesLabel.setBounds(250,105,60,15);
        endMinutesLabel.setBounds(250,155,60,15);
        intervalLabel.setBounds(120, 215, 180, 15);

        titleField.setBounds(30,20,280,30);
        descriptionField.setBounds(30,70,280,30);
        loginField.setBounds(30,20,280,30);
        intervalField.setBounds(120, 230, 180, 30);

        activeBox.setBounds(30, 230, 80, 30);

        startHoursSpinner.setBounds(180,120,60,30);
        startMinutesSpinner.setBounds(250,120,60,30);
        endHoursSpinner.setBounds(180,170,60,30);
        endMinutesSpinner.setBounds(250,170,60,30);

        confirmButton.setBounds(30,300,280,40);
        unrepeatableTaskFormButton.setBounds(30,300,200,40);
        repeatableTaskFormButton.setBounds(30,350,200,40);
        editTaskFormButton.setBounds(300,300,200,40);
        deleteTaskFormButton.setBounds(300,350,200,40);
        calendarFormButton.setBounds(570,300,200,40);

        cancelButton.setBounds(30,350,280,40);
        exitButton.setBounds(570,350,200,40);

        mainPanel.setBackground(Color.green);
        mainPanel.add(unrepeatableTaskFormButton);
        mainPanel.add(repeatableTaskFormButton);
        mainPanel.add(editTaskFormButton);
        mainPanel.add(deleteTaskFormButton);
        mainPanel.add(calendarFormButton);
        mainPanel.add(exitButton);

        mainPanel.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(mainPanel);
    }

    public void showTaskList(String taskList) {
        tasksField.setText(taskList);
    }

    /**
     * method where we flush fields
     */
    protected void flushFields() {
        titleField.setText("");
        descriptionField.setText("");
        startHoursSpinner.setValue(1);
        endHoursSpinner.setValue(1);
        startMinutesSpinner.setValue(0);
        endMinutesSpinner.setValue(0);
    }

    /**
     * method where we parse time to date
     * @param calendar is input date
     * @param hours is input hour
     * @param minutes is input minute
     * @throws ParseException if we can't parse it
     * @return date
     */
    protected Date parseTimeToDate(JDatePickerImpl calendar, JSpinner hours, JSpinner minutes) throws ParseException {
        String datePattern = "yyyy-MM-dd HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

        StringBuilder sb = new StringBuilder(String.valueOf(calendar.getModel().getYear()));
        sb.append("-");
        sb.append(String.valueOf(calendar.getModel().getMonth() + 1));
        sb.append("-");
        sb.append(String.valueOf(calendar.getModel().getDay()));
        sb.append(" ");
        sb.append(String.valueOf(hours.getModel().getValue()));
        sb.append(":");
        sb.append(String.valueOf(minutes.getModel().getValue()));
        return dateFormat.parse(sb.toString());
    }

    /**
     * method for open panel or form
     */
    public void open() {
        this.setVisible(true);
    }

    /**
     * method for close panel or form
     */
    public void close() {
        this.setVisible(false);
    }

    /**
     * method where we add listener for buttons or boxes
     * @param actionListener is listener
     */
    public void addListener(ActionListener actionListener) {
        unrepeatableTaskFormButton.addActionListener(actionListener);
        repeatableTaskFormButton.addActionListener(actionListener);
        activeBox.addActionListener(actionListener);
        editTaskFormButton.addActionListener(actionListener);
        deleteTaskFormButton.addActionListener(actionListener);

        cancelButton.addActionListener(actionListener);
        exitButton.addActionListener(actionListener);
        calendarFormButton.addActionListener(actionListener);
    }

    /**
     * method where we show error message
     * @param errorMessage is message to show
     */
    public void displayErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage);
    }

    /**
     * method where we show message
     * @param message is message to show
     */
    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}

