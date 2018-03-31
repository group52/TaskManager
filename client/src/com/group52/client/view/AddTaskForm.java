package com.group52.client.view;

import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

/**
 * class AddTaskForm is class for create repeat and unrepeat task forms
 */
public class AddTaskForm extends MainPanel implements Listenable {

    private JFrame taskForm;
    private JDatePickerImpl startDatePicker = new Calendar(false).getDatePicker();
    private JDatePickerImpl endDatePicker = new Calendar(false).getDatePicker();
    public JButton unrepeatableTaskButton = new JButton("Create task");
    public JButton repeatableTaskButton = new JButton("Create task");

    /**
     * creating add task form constructor
     * @see AddTaskForm
     * @param taskType can be "Repeatable" or "Unrepeatable"
     */
    public AddTaskForm(String taskType) {
        taskForm = new JFrame("Add task");
        taskForm.setSize(350,450);
        taskForm.setLocationRelativeTo(null);
        taskForm.setResizable(false);
        URL iconPath = getClass().getClassLoader().getResource("icon.png");
        taskForm.setIconImage(Toolkit.getDefaultToolkit().getImage(iconPath));

        startDatePicker.setBounds(30,120,140,30);
        endDatePicker.setBounds(30,170,140,30);

        activeBox.setSelected(true);
        taskForm.add(titleField);
        taskForm.add(descriptionField);

        taskForm.add(startDatePicker);
        taskForm.add(startHoursSpinner);
        taskForm.add(startMinutesSpinner);
        taskForm.add(startDateLabel);
        taskForm.add(startHoursLabel);
        taskForm.add(startMinutesLabel);

        taskForm.add(activeBox);
        taskForm.add(titleLabel);
        taskForm.add(descriptionLabel);
        taskForm.add(cancelButton);
        taskForm.setLayout(null);

        if (taskType.equals("Unrepeatable")) {
            unrepeatableTaskButton.setBounds(30,300,280,40);
            taskForm.add(unrepeatableTaskButton);


        }
        else if (taskType.equals("Repeatable")) {
            repeatableTaskButton.setBounds(30,300,280,40);
            taskForm.add(repeatableTaskButton);
            taskForm.add(endDatePicker);
            taskForm.add(endHoursSpinner);
            taskForm.add(endMinutesSpinner);
            taskForm.add(intervalField);

            taskForm.add(endDateLabel);
            taskForm.add(endDateLabel);
            taskForm.add(endHoursLabel);
            taskForm.add(endMinutesLabel);
            taskForm.add(intervalLabel);

        }
    }

    /**
     * method where we get title of task
     * @throws NullPointerException if title field is empty
     * @return title
     */
    public String getTitle() throws NullPointerException {
        if (titleField.getText().isEmpty()) throw new NullPointerException("Title field is empty");
        return titleField.getText();
    }

    /**
     * method where we get description of task
     * @throws NullPointerException if description field is empty
     * @return description
     */
    public String getDescription() throws NullPointerException {
        if (descriptionField.getText().isEmpty()) throw new NullPointerException("Description field is empty");
        return descriptionField.getText();
    }

    /**
     * method where we get start time of task
     * @throws ParseException if we can't parse it
     * @return start time
     */
    public long getStartTime() throws ParseException {
        Date parsingDate = parseTimeToDate(startDatePicker, startHoursSpinner, startMinutesSpinner);
        return parsingDate.getTime();
    }

    /**
     * method where we get end time of task
     * @throws ParseException if we can't parse it
     * @return end time
     */
    public long getEndTime() throws ParseException {
        Date parsingDate = parseTimeToDate(endDatePicker, endHoursSpinner, endMinutesSpinner);
        return parsingDate.getTime();
    }

    /**
     * method where we get interval of task
     * @throws IllegalArgumentException if interval < 0
     * @throws NullPointerException if interval field is empty
     * @return end interval
     */
    public int getInterval() throws IllegalArgumentException, NullPointerException {
        if (intervalField.getText().isEmpty())
            throw new NullPointerException("Interval field is empty");
        int interval = Integer.parseInt(intervalField.getText());
        if (interval < 0)
            throw new IllegalArgumentException("Interval can not be less than zero");
        return interval;
    }

    @Override
    public void open() {
        flushFields();
        taskForm.setVisible(true);
    }

    @Override
    public void close() {
        taskForm.setVisible(false);
    }

    @Override
    public void addListener(ActionListener actionListener) {
        unrepeatableTaskButton.addActionListener(actionListener);
        repeatableTaskButton.addActionListener(actionListener);
        cancelButton.addActionListener(actionListener);
    }
}
