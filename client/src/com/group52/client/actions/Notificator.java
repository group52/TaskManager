package com.group52.client.actions;

import com.group52.client.view.NotificationForm;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
/**
 * class Notificator to notify user about tasks
 */
public class Notificator extends Thread {

    private static Logger log = Logger.getLogger(Handler.class);
    private NotificationForm notificationForm;
    private List<XMLParse.Task> taskList = new ArrayList<>();
    private XMLParse.Task taskToPostpone = new XMLParse.Task();
    private boolean doWork;

    @Override
    public void run() {
        while (doWork) {
            long curTime = System.currentTimeMillis();
            if (taskList != null) {
                for (XMLParse.Task task : taskList) {
                    if (task != null) {
                        if (curTime - task.getTime() > 0 && curTime - task.getTime() < 500) {
                            notificationForm.showTask("Time for doing: " + task.getTitle()
                                    + "\nTime: " + new Date(task.getTime()) + "\n");
                            setTaskToPostpone(task);
                            notificationForm.open();
                        }
                    }
                }
            }
            try {
                sleep(500);
            } catch (InterruptedException e) {
                log.error("InterruptedException exceptions: ", e);
            }
        }
    }

    /**
     * method where we get task to postpone
     * @return taskToPostpone
     */
    public XMLParse.Task getTaskToPostpone() {
        return taskToPostpone;
    }

    /**
     * method where we set task to postpone
     * @param taskToPostpone is task to postpone
     */
    public void setTaskToPostpone(XMLParse.Task taskToPostpone) {
        this.taskToPostpone = taskToPostpone;
    }

    /**
     * method where we get task list
     * @return taskList
     */
    public List<XMLParse.Task> getTaskList() {
        return taskList;
    }

    /**
     * method where we set task list
     * @param taskList is task list
     */
    public void setTaskList(List<XMLParse.Task> taskList) {
        this.taskList = taskList;
    }

    /**
     * method where we get notification form
     * @return notificationForm
     */
    public NotificationForm getNotificationForm() {
        return notificationForm;
    }

    /**
     * method where we set notification form
     * @param notificationForm is notification form
     */
    public void setNotificationForm(NotificationForm notificationForm) {
        this.notificationForm = notificationForm;
    }

    /**
     * method where we set work threads's status
     * @param doWork is boolean value(true or false)
     */
    public void setWork(boolean doWork) {
        this.doWork = doWork;
    }

}
