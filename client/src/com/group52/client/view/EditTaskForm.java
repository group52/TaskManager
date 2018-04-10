package com.group52.client.view;

import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * class EditTaskForm is class for create edit task form
 */
public class EditTaskForm extends MainPanel implements Listenable {

    private JFrame taskForm;
    private JDatePickerImpl startDatePicker = new Calendar(false).getDatePicker();
    private JDatePickerImpl endDatePicker = new Calendar(false).getDatePicker();
    public JButton editTaskButton = new JButton("Edit task");

    /**
     * creating edit task form constructor
     * @see EditTaskForm
     */
    public EditTaskForm() {
        taskForm = new JFrame("Edit task");
        taskForm.setSize(700,450);
        taskForm.setLocationRelativeTo(null);
        taskForm.setResizable(false);
        URL iconPath = getClass().getClassLoader().getResource("icon.png");
        taskForm.setIconImage(Toolkit.getDefaultToolkit().getImage(iconPath));

        startDatePicker.setBounds(30,120,140,30);
        endDatePicker.setBounds(30,170,140,30);

        activeBox.setSelected(true);
        taskForm.add(titleField);
        taskForm.add(descriptionField);

        JScrollPane areaScrollPane = new JScrollPane(comboBox);
        areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        areaScrollPane.setBounds(350, 150, 300,100);
        taskForm.getContentPane().add(areaScrollPane);

        taskForm.add(startDatePicker);
        taskForm.add(startHoursSpinner);
        taskForm.add(startMinutesSpinner);
        taskForm.add(startDateLabel);
        taskForm.add(startHoursLabel);
        taskForm.add(startMinutesLabel);

        taskForm.add(endDatePicker);
        taskForm.add(endHoursSpinner);
        taskForm.add(endMinutesSpinner);
        taskForm.add(endDateLabel);
        taskForm.add(endHoursLabel);
        taskForm.add(endMinutesLabel);

        taskForm.add(intervalField);
        taskForm.add(intervalLabel);

        taskForm.add(activeBox);
        taskForm.add(titleLabel);
        taskForm.add(descriptionLabel);

        JLabel img;
        URL imgPath = getClass().getClassLoader().getResource("choose.gif");
        if (imgPath != null) {
            img = new JLabel(new ImageIcon(imgPath));
            img.setBounds(350, 30, 300,100);
            taskForm.add(img);
        }

        editTaskButton.setBounds(30,300,280,40);
        taskForm.add(editTaskButton);
        taskForm.add(cancelButton);
        taskForm.setLayout(null);
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
     * @return end interval in millis
     */
    public int getInterval() throws IllegalArgumentException, NullPointerException {
        if (intervalField.getText().isEmpty())
            throw new NullPointerException("Interval field is empty");
        int interval = Integer.parseInt(intervalField.getText());
        if (interval < 0)
            throw new IllegalArgumentException("Interval can not be less than zero");
        interval *= 1000; //parse to millis
        return interval;
    }

    /**
     * method where we add repeatable fields
     */
    public void addRepeatableFields() {
        endDatePicker.setVisible(true);
        endHoursSpinner.setVisible(true);
        endMinutesSpinner.setVisible(true);
        intervalField.setVisible(true);

        endDateLabel.setVisible(true);
        endDateLabel.setVisible(true);
        endHoursLabel.setVisible(true);
        endMinutesLabel.setVisible(true);
        intervalLabel.setVisible(true);
    }

    /**
     * method where we remove repeatable fields
     */
    public void removeRepeatableFields() {
        endDatePicker.setVisible(false);
        endHoursSpinner.setVisible(false);
        endMinutesSpinner.setVisible(false);
        intervalField.setVisible(false);;

        endDateLabel.setVisible(false);
        endDateLabel.setVisible(false);
        endHoursLabel.setVisible(false);
        endMinutesLabel.setVisible(false);
        intervalLabel.setVisible(false);
    }

    /**
     * method where we add task's info to fields
     */
    public void addTaskInfo(String title, String description, int interval) {
        flushFields();
        titleField.setText(title);
        descriptionField.setText(description);
        intervalField.setText(String.valueOf(interval/1000));
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
        editTaskButton.addActionListener(actionListener);
        cancelButton.addActionListener(actionListener);
        comboBox.addActionListener(actionListener);
    }
}
