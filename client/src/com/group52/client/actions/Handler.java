package com.group52.client.actions;

import com.group52.client.view.*;
import com.group52.client.view.Calendar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.rmi.ServerException;
import java.util.*;
import org.apache.log4j.*;
import javax.swing.*;
import javax.xml.bind.JAXBException;

/**
 * class Handler for manage all actions
 */
public class Handler {

    private Logger log = Logger.getLogger(Handler.class);
    private MainPanel mainPanel;
    private ServerDialog serverDialog;
    private Notificator notificator;

    /**
     * creating handler constructor
     * @see Handler
     * @param mainPanel is panel with tasks field and buttons
     * @param serverDialog is socket for send and get xml files
     * @param notificator is entity for show notification of current tasks
     */
    public Handler(MainPanel mainPanel, ServerDialog serverDialog, Notificator notificator) {
        this.mainPanel = mainPanel;
        this.serverDialog = serverDialog;
        this.notificator = notificator;
        new Handler.Listener();
    }

    /**
     * method where we get response from server
     * @throws ServerException if server has a problem
     * @throws JAXBException if JAXB parser has a problem
     * @return file
     */
    private BufferedWriter getResponseFromServer() throws IOException, JAXBException {
        BufferedWriter bw = serverDialog.getResponseFromServer();
        int code = XMLParse.getCodeFromXML(bw);
        String status = XMLParse.getStatusFromXML(bw);
        if (code == 400 || code == 401 || code == 404 || code == 405 || code == 415 || code == 500)
            throw new ServerException(status);
        return bw;
    }

    /**
     * method for update TaskList
     */
    private void updateTaskList () throws IOException, JAXBException {
            serverDialog.sendXMLToServer(XMLParse.parseRequestToXML("view"));
            BufferedWriter bw = serverDialog.getResponseFromServer();
            if (XMLParse.getActionFromXML(bw).equals("view"))
            mainPanel.showTaskList(XMLParse.getTasksFromXML((bw)));

            serverDialog.sendXMLToServer(XMLParse.parseRequestToXML("notification"));
            bw = serverDialog.getResponseFromServer();
            if (XMLParse.getActionFromXML(bw).equals("notification"))
                notificator.setTaskList(XMLParse.getTasks(bw));
    }

    /**
     * inner class Listener for manage on click actions
     */
    public class Listener implements ActionListener {

        private WelcomeForm welcomeForm = new WelcomeForm();
        private SignUpForm signUpForm = new SignUpForm();
        private SignInForm signInForm = new SignInForm();
        private AddTaskForm unrepeatableTaskForm = new AddTaskForm("Unrepeatable");
        private AddTaskForm repeatableTaskForm = new AddTaskForm("Repeatable");
        private EditTaskForm editTaskForm = new EditTaskForm();
        private DeleteTaskForm deleteTaskForm = new DeleteTaskForm();
        private NotificationForm notificationForm = new NotificationForm();

        /**
         * constructor without arguments
         * @see Listener
         */
        public Listener() {
            Listener listener = this;
            mainPanel.addListener(listener);
            welcomeForm.addListener(listener);
            signUpForm.addListener(listener);
            signInForm.addListener(listener);
            unrepeatableTaskForm.addListener(listener);
            repeatableTaskForm.addListener(listener);
            editTaskForm.addListener(listener);
            deleteTaskForm.addListener(listener);
            notificationForm.addListener(listener);
        }

        /**
         * method for edit tasks to combo box
         * @param comboBox is box for choose task
         * @throws ServerException if server has a problem
         * @throws JAXBException if JAXB parser has a problem
         */
        private void editTasksToComboBox (JComboBox comboBox) throws IOException, JAXBException {
            serverDialog.sendXMLToServer(XMLParse.parseRequestToXML("view"));
            List<XMLParse.Task> tasks = XMLParse.getTasks(getResponseFromServer());
            comboBox.removeAllItems();
            for (XMLParse.Task task: tasks) {
                comboBox.addItem(task);
            }
        }

        /**
         * method for responding to a button click
         * @param event is action event
         */
        public void actionPerformed(ActionEvent event) {
            notificator.setNotificationForm(notificationForm);
            try {
                if (event.getSource().equals(signUpForm.confirmButton)) {
                    String login = signUpForm.getLogin();
                    String password = signUpForm.getPassword();
                    String repeatedPassword = signUpForm.getRepeatedPassword();

                    if (!password.equals(repeatedPassword))
                        throw new IOException("Passwords don't match");
                    else XMLParse.createClient(login, password, 0);

                    serverDialog.sendXMLToServer(XMLParse.parseRequestToXML("oneMoreUser"));
                    BufferedWriter response = getResponseFromServer();
                    int code = XMLParse.getCodeFromXML(response);
                    String status = XMLParse.getStatusFromXML(response);
                    if(code == 200 || code == 201 || code == 202) {
                        mainPanel.displayMessage(status);
                        XMLParse.setId(XMLParse.getUserIdFromXML(response));
                        signUpForm.close();
                        welcomeForm.close();
                        updateTaskList();
                        mainPanel.open();
                    }
                }

                if (event.getSource().equals(signInForm.confirmButton)) {
                    String login = signInForm.getLogin();
                    String password = signInForm.getPassword();
                    XMLParse.createClient(login, password, 0);
                    serverDialog.sendXMLToServer(XMLParse.parseRequestToXML("user"));

                    BufferedWriter response = getResponseFromServer();
                    int code = XMLParse.getCodeFromXML(response);
                    String status = XMLParse.getStatusFromXML(response);
                    if(code == 200 || code == 201 || code == 202) {
                        mainPanel.displayMessage(status);
                        XMLParse.setId(XMLParse.getUserIdFromXML(response));
                        signInForm.close();
                        welcomeForm.close();
                        updateTaskList();
                        mainPanel.open();
                    }
                }
                if (event.getSource().equals(unrepeatableTaskForm.unrepeatableTaskButton)) {
                    String title = unrepeatableTaskForm.getTitle();
                    String description = unrepeatableTaskForm.getDescription();
                    long time = unrepeatableTaskForm.getStartTime();
                    boolean active = unrepeatableTaskForm.activeBox.isSelected();
                    serverDialog.sendXMLToServer(XMLParse.parseTaskToXML("add",
                            title, description, time, 0, 0, 0, active));
                    updateTaskList();
                    unrepeatableTaskForm.close();
                }

                if (event.getSource().equals(repeatableTaskForm.repeatableTaskButton)) {
                    String title = repeatableTaskForm.getTitle();
                    String description = repeatableTaskForm.getDescription();
                    long start = repeatableTaskForm.getStartTime();
                    long end = repeatableTaskForm.getEndTime();
                    int interval = repeatableTaskForm.getInterval();
                    boolean active = repeatableTaskForm.activeBox.isSelected();
                    serverDialog.sendXMLToServer(XMLParse.parseTaskToXML("add",
                            title, description, 0, start, end, interval, active));
                    updateTaskList();
                    repeatableTaskForm.close();
                }

                if (event.getSource().equals(editTaskForm.editTaskButton)) {
                    XMLParse.Task oldTask = (XMLParse.Task) editTaskForm.comboBox.getModel().getSelectedItem();
                    String title = editTaskForm.getTitle();
                    String description = editTaskForm.getDescription();
                    long time = 0;
                    long start = 0;
                    long end = 0;
                    int interval = 0;
                    if (oldTask.getInterval() != 0) {
                        start = editTaskForm.getStartTime();
                        end = editTaskForm.getEndTime();
                        interval = editTaskForm.getInterval();
                    } else time = editTaskForm.getStartTime();
                    boolean active = editTaskForm.activeBox.isSelected();
                    serverDialog.sendXMLToServer(XMLParse.parseTaskToXML("edit",
                            oldTask, title, description, time, start, end, interval, active));
                    updateTaskList();
                    editTaskForm.close();
                }

                if (event.getSource().equals(deleteTaskForm.deleteTaskButton)) {
                    XMLParse.Task task = (XMLParse.Task) deleteTaskForm.comboBox.getModel().getSelectedItem();
                    serverDialog.sendXMLToServer(XMLParse.parseTaskToXML("delete", task));
                    updateTaskList();
                    deleteTaskForm.close();
                }

                if (event.getSource().equals(notificationForm.postponeTaskButton)) {
                    XMLParse.Task task = notificator.getTaskToPostpone();
                    long time = 0;
                    long start = 0;
                    if (task.getInterval() == 0)  time = task.getTime() + 300000;
                    else start = task.getStart() + 300000;
                    serverDialog.sendXMLToServer(XMLParse.parseTaskToXML("edit",
                            task, task.getTitle(), task.getDescription(), time,
                            start, task.getEnd(), task.getInterval(), task.isActive()));
                    updateTaskList();
                    notificationForm.close();
                }

                if (event.getSource().equals(mainPanel.calendarFormButton)) {
                    new Calendar(true);
                }
                if (event.getSource().equals(welcomeForm.signUpButton)) signUpForm.open();
                if (event.getSource().equals(signUpForm.cancelButton)) signUpForm.close();

                if (event.getSource().equals(welcomeForm.signInButton)) signInForm.open();
                if (event.getSource().equals(signInForm.cancelButton)) signInForm.close();

                if (event.getSource().equals(mainPanel.unrepeatableTaskFormButton)) unrepeatableTaskForm.open();
                if (event.getSource().equals(unrepeatableTaskForm.cancelButton)) unrepeatableTaskForm.close();

                if (event.getSource().equals(mainPanel.repeatableTaskFormButton)) repeatableTaskForm.open();
                if (event.getSource().equals(repeatableTaskForm.cancelButton)) repeatableTaskForm.close();

                if (event.getSource().equals(mainPanel.editTaskFormButton)) {
                    editTasksToComboBox(editTaskForm.comboBox);
                    editTaskForm.open();
                }
                if (event.getSource().equals(editTaskForm.cancelButton)) editTaskForm.close();

                if (event.getSource().equals(editTaskForm.comboBox)) {
                    XMLParse.Task task = (XMLParse.Task) editTaskForm.comboBox.getModel().getSelectedItem();
                    if (task.getInterval() == 0) editTaskForm.removeRepeatableFields();
                    else editTaskForm.addRepeatableFields();
                }

                if (event.getSource().equals(mainPanel.deleteTaskFormButton)) {
                    editTasksToComboBox(deleteTaskForm.comboBox);
                    deleteTaskForm.open();
                }
                if (event.getSource().equals(deleteTaskForm.cancelButton)) deleteTaskForm.close();

                if (event.getSource().equals(notificationForm.closeTaskButton)) notificationForm.close();

                if (event.getSource().equals(mainPanel.exitButton)) {
                    log.info("Logout");
                    serverDialog.sendXMLToServer(XMLParse.parseRequestToXML("close"));
                    serverDialog.close();
                    mainPanel.setVisible(false);
                    mainPanel.dispose();
                    //System.exit(0);
                    Main.main(new String[]{});
                }
            } catch (JAXBException jaxb) {
                mainPanel.displayErrorMessage("Parse exception");
                log.error("JAXBException" + jaxb);
                jaxb.printStackTrace();
            } catch (ServerException se) {
                mainPanel.displayErrorMessage(se.getMessage());
                log.error("Server exception" + se);
                se.printStackTrace();
            } catch (IllegalArgumentException iae) {
                mainPanel.displayErrorMessage("IllegalArgumentException: " + iae.getMessage());
                log.error("IllegalArgumentException: ", iae);
                iae.printStackTrace();
            } catch (NullPointerException npe) {
                mainPanel.displayErrorMessage("NullPointerException: " + npe.getMessage());
                log.error("NullPointerException: ", npe);
                npe.printStackTrace();
            } catch (IndexOutOfBoundsException ioe) {
                mainPanel.displayErrorMessage("IndexOutOfBoundsException");
                log.error("IndexOutOfBoundsException: ", ioe);
                ioe.printStackTrace();
            } catch (NoSuchElementException nse) {
                mainPanel.displayErrorMessage(nse.getMessage());
                log.error("NoSuchElementException: ", nse);
                nse.printStackTrace();
            } catch (IOException io) {
                mainPanel.displayErrorMessage(io.getMessage());
                log.error("IOException: ", io);
                io.printStackTrace();
            } catch (Exception ex) {
                mainPanel.displayErrorMessage(ex.getMessage());
                log.error("Exception: ", ex);
                ex.printStackTrace();
            }
        }
    }
}




