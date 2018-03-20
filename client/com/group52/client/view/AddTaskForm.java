package com.group52.client.view;

import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;

public class AddTaskForm extends MainPanel implements Listenable {

    private JFrame taskForm;
    private JDatePickerImpl startDatePicker = new Calendar(false).getDatePicker();
    private JDatePickerImpl endDatePicker = new Calendar(false).getDatePicker();
    public JButton unrepeatableTaskButton = new JButton("Create task");
    public JButton repeatableTaskButton = new JButton("Create task");

    public AddTaskForm(String taskType) {
        taskForm = new JFrame();
        taskForm.setSize(350,450);
        taskForm.setLocationRelativeTo(null);
        taskForm.setResizable(false);

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

    public String getTitle() throws NullPointerException {
        return titleField.getText();
    }

    public String getDescription() {
        return descriptionField.getText();
    }

    public long getStartTime() throws ParseException {
        Date parsingDate = parseTimeToDate(startDatePicker, startHoursSpinner, startMinutesSpinner);
        return parsingDate.getTime();
    }

    public long getEndTime() throws ParseException {
        Date parsingDate = parseTimeToDate(endDatePicker, endHoursSpinner, endMinutesSpinner);
        return parsingDate.getTime();
    }

    public int getInterval()  {
        return Integer.parseInt(intervalField.getText());
    }

    public void open() {
        flushFields();
        taskForm.setVisible(true);
    }

    public void close() {
        taskForm.setVisible(false);
    }

    public void addListener(ActionListener actionListener) {
        unrepeatableTaskButton.addActionListener(actionListener);
        repeatableTaskButton.addActionListener(actionListener);
        cancelButton.addActionListener(actionListener);
    }
}
