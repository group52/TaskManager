package com.group52.client.view;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

public class Calendar extends  MainPanel {

    private JDatePickerImpl datePicker;

    public Calendar(boolean isCalendarField) {
        UtilDateModel model = new UtilDateModel();

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        if (isCalendarField) {
            JFrame frame = new JFrame();
            frame.setSize(300, 300);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
            frame.add(datePanel);
        }
    }

    public JDatePickerImpl getDatePicker() {
        return datePicker;
    }

    public String getTime() {
        Date selectedDate = (Date) datePicker.getModel().getValue();
        return selectedDate.toString();
    }

    public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private String datePattern = "yyyy-MM-dd";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                GregorianCalendar cal = (GregorianCalendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }

    }
}
