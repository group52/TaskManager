package com.group52.client.view;

import java.awt.event.ActionListener;

public class EditTaskForm implements Listenable, Closeable {

    public EditTaskForm() { }

    public void showOldTitle(String title) { }

    public String getNewTitle() {
        return "title";
    }

    public void showOldTime(String time) { }

    public String getNewTime() {
        return "time";
    }

    public void showOldDescription(String time) { }

    public String getNewDescription() {
        return "Description";
    }

    public void close () { }

    public void addListener(ActionListener actionListener) { }

}
