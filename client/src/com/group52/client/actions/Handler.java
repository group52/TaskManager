package com.group52.client.actions;

import com.group52.client.view.*;
import com.group52.client.view.Calendar;

import java.awt.event.*;
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
     *
     * @param mainPanel    is panel with tasks field and buttons
     * @param serverDialog is socket for send and get xml files
     * @param notificator  is entity for show notification of current tasks
     * @see Handler
     */
    public Handler(MainPanel mainPanel, ServerDialog serverDialog, Notificator notificator) {
        this.mainPanel = mainPanel;
        this.serverDialog = serverDialog;
        this.notificator = notificator;
        new Handler.Listener();
    }

    /**
     * method where we get response from server
     *
     * @return file
     * @throws ServerException if server has a problem
     * @throws JAXBException   if JAXB parser has a problem
     */
    private String getResponseFromServer() throws IOException, JAXBException {
        String s = serverDialog.getResponseFromServer();
        int code = XMLParse.getCodeFromXML(s);
        String status = XMLParse.getStatusFromXML(s);
        if (code == 400 || code == 401 || code == 404 || code == 405 || code == 415 || code == 500)
            throw new ServerException(status);
        else mainPanel.displayMessage(status);
        return s;
    }

    /**
     * method for update TaskList
     *
     * @throws JAXBException if JAXB parser has a problem
     */
    private void updateTaskList() throws JAXBException {
        serverDialog.sendXMLToServer(XMLParse.parseRequestToXML("view"));
        String s = serverDialog.getResponseFromServer();
        if (s != null) {
            if ((XMLParse.getActionFromXML(s).equals("view")))
                mainPanel.showTaskList(XMLParse.getTasksFromXML((s)));

            serverDialog.sendXMLToServer(XMLParse.parseRequestToXML("notification"));
            s = serverDialog.getResponseFromServer();
            if (XMLParse.getActionFromXML(s).equals("notification")) ;
            {
                notificator.setTaskList(XMLParse.getTasks(s));
            }
        }
    }

    /**
     * inner class Listener for manage on click actions
     */
    public class Listener extends WindowAdapter implements ActionListener {

        private SignUpForm signUpForm = new SignUpForm();
        private SignInForm signInForm = new SignInForm();
        private AddTaskForm unrepeatableTaskForm = new AddTaskForm("Unrepeatable");
        private AddTaskForm repeatableTaskForm = new AddTaskForm("Repeatable");
        private EditTaskForm editTaskForm = new EditTaskForm();
        private DeleteTaskForm deleteTaskForm = new DeleteTaskForm();
        private NotificationForm notificationForm = new NotificationForm();
        private WelcomeForm welcomeForm = new WelcomeForm();

        /**
         * constructor without arguments
         *
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
            mainPanel.addWindowListener(listener);
            welcomeForm.addWindowListener(listener);
        }

        /**
         * method for edit tasks to combo box
         *
         * @param comboBox is box for choose task
         * @throws ServerException if server has a problem
         * @throws JAXBException   if JAXB parser has a problem
         */
        private void editTasksToComboBox(JComboBox comboBox) throws IOException, JAXBException {
            serverDialog.sendXMLToServer(XMLParse.parseRequestToXML("view"));
            List<XMLParse.Task> tasks = XMLParse.getTasks(getResponseFromServer());
            comboBox.removeAllItems();
            for (XMLParse.Task task : tasks) {
                comboBox.addItem(task);
            }
        }

        /**
         * method for responding to a button click
         *
         * @param event is action event
         */
        public void actionPerformed(ActionEvent event) {
            notificator.setNotificationForm(notificationForm);
            try {
                if (signUpForm.confirmButton.equals(event.getSource())) {
                    String login = signUpForm.getLogin();
                    String password = signUpForm.getPassword();
                    String repeatedPassword = signUpForm.getRepeatedPassword();
                    if (!password.equals(repeatedPassword))
                        throw new IOException("Passwords don't match");
                    else XMLParse.createClient(login, password, 0);
                    serverDialog.sendXMLToServer(XMLParse.parseRequestToXML("oneMoreUser"));

                    String response = getResponseFromServer();
                    XMLParse.setId(XMLParse.getUserIdFromXML(response));
                    signUpForm.close();
                    welcomeForm.close();
                    mainPanel.open();
                }
                if (signInForm.confirmButton.equals(event.getSource())) {
                    String login = signInForm.getLogin();
                    String password = signInForm.getPassword();
                    XMLParse.createClient(login, password, 0);
                    serverDialog.sendXMLToServer(XMLParse.parseRequestToXML("user"));

                    String response = getResponseFromServer();
                    XMLParse.setId(XMLParse.getUserIdFromXML(response));
                    updateTaskList();
                    signInForm.close();
                    welcomeForm.close();
                    mainPanel.open();
                }
                if (welcomeForm.ipButton.equals(event.getSource())) {
                    log.info("changing server IP address");
                    welcomeForm.close();
                    notificator.setWork(false);
                    serverDialog.sendXMLToServer(XMLParse.parseRequestToXML("close"));
                    serverDialog.close();
                    Main.writeIPToFile("");
                    Main.main(new String[]{});
                }

                if (unrepeatableTaskForm.unrepeatableTaskButton.equals(event.getSource())) {
                    String title = unrepeatableTaskForm.getTitle();
                    String description = unrepeatableTaskForm.getDescription();
                    long time = unrepeatableTaskForm.getStartTime();
                    boolean active = unrepeatableTaskForm.activeBox.isSelected();
                    serverDialog.sendXMLToServer(XMLParse.parseTaskToXML("add",
                            title, description, time, 0, 0, 0, active));

                    getResponseFromServer();
                    unrepeatableTaskForm.close();
                    updateTaskList();
                }

                if (repeatableTaskForm.repeatableTaskButton.equals(event.getSource())) {
                    String title = repeatableTaskForm.getTitle();
                    String description = repeatableTaskForm.getDescription();
                    long start = repeatableTaskForm.getStartTime();
                    long end = repeatableTaskForm.getEndTime();
                    int interval = repeatableTaskForm.getInterval();
                    if (start > end)
                        throw new IOException("Start can't be after end");
                    boolean active = repeatableTaskForm.activeBox.isSelected();
                    serverDialog.sendXMLToServer(XMLParse.parseTaskToXML("add",
                            title, description, 0, start, end, interval, active));

                    getResponseFromServer();
                    repeatableTaskForm.close();
                    updateTaskList();
                }

                if (editTaskForm.editTaskButton.equals(event.getSource())) {
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
                    if (start > end)
                        throw new IOException("Start can't be after end");
                    serverDialog.sendXMLToServer(XMLParse.parseTaskToXML("edit",
                            oldTask, title, description, time, start, end, interval, active));

                    getResponseFromServer();
                    editTaskForm.close();
                    updateTaskList();
                }

                if (deleteTaskForm.deleteTaskButton.equals(event.getSource())) {
                    XMLParse.Task task = (XMLParse.Task) deleteTaskForm.comboBox.getModel().getSelectedItem();
                    serverDialog.sendXMLToServer(XMLParse.parseTaskToXML("delete", task));

                    getResponseFromServer();
                    deleteTaskForm.close();
                    updateTaskList();
                }

                if (notificationForm.postponeTaskButton.equals(event.getSource())) {
                    XMLParse.Task task = notificator.getTaskToPostpone();
                    long time = task.getTime() + 300000;
                    serverDialog.sendXMLToServer(XMLParse.parseTaskToXML("add", task.getTitle(),
                            task.getDescription(), time, 0, 0, 0, task.isActive()));

                    getResponseFromServer();
                    notificationForm.close();
                    updateTaskList();
                }

                if (mainPanel.calendarFormButton.equals(event.getSource())) {
                    new Calendar(true);
                }
                if (welcomeForm.signUpButton.equals(event.getSource())) signUpForm.open();
                if (signUpForm.cancelButton.equals(event.getSource())) signUpForm.close();

                if (welcomeForm.signInButton.equals(event.getSource())) signInForm.open();
                if (signInForm.cancelButton.equals(event.getSource())) signInForm.close();

                if (mainPanel.unrepeatableTaskFormButton.equals(event.getSource())) unrepeatableTaskForm.open();
                if (unrepeatableTaskForm.cancelButton.equals(event.getSource())) unrepeatableTaskForm.close();

                if (mainPanel.repeatableTaskFormButton.equals(event.getSource())) repeatableTaskForm.open();
                if (repeatableTaskForm.cancelButton.equals(event.getSource())) repeatableTaskForm.close();

                if (mainPanel.editTaskFormButton.equals(event.getSource())) {
                    editTasksToComboBox(editTaskForm.comboBox);
                    XMLParse.Task task = (XMLParse.Task) editTaskForm.comboBox.getModel().getElementAt(0);
                    editTaskForm.addTaskInfo(task.getTitle(), task.getDescription());
                    editTaskForm.open();
                }
                if (editTaskForm.cancelButton.equals(event.getSource())) editTaskForm.close();

                if (editTaskForm.comboBox.equals(event.getSource())) {
                    XMLParse.Task task = (XMLParse.Task) editTaskForm.comboBox.getModel().getSelectedItem();
                    editTaskForm.addTaskInfo(task.getTitle(), task.getDescription());
                    if (task.getInterval() == 0) editTaskForm.removeRepeatableFields();
                    else editTaskForm.addRepeatableFields();
                }

                if (mainPanel.deleteTaskFormButton.equals(event.getSource())) {
                    editTasksToComboBox(deleteTaskForm.comboBox);
                    deleteTaskForm.open();
                }
                if (deleteTaskForm.cancelButton.equals(event.getSource())) deleteTaskForm.close();

                if (notificationForm.closeTaskButton.equals(event.getSource())) {
                    notificationForm.close();
                    updateTaskList();
                }

                if (mainPanel.exitButton.equals(event.getSource())) {
                    log.info("Logout");
                    serverDialog.sendXMLToServer(XMLParse.parseRequestToXML("close"));
                    serverDialog.close();
                    notificator.setWork(false);
                    mainPanel.setVisible(false);
                    mainPanel.dispose();
                    Main.main(new String[]{});
                }
            } catch (JAXBException jaxb) {
                mainPanel.displayErrorMessage("Parse exception");
                log.error("JAXBException: " + jaxb);
            } catch (ServerException se) {
                mainPanel.displayErrorMessage(se.getMessage());
                log.error("Server exception: " + se);
            } catch (IllegalArgumentException iae) {
                mainPanel.displayErrorMessage(iae.getMessage());
                log.error("IllegalArgumentException: ", iae);
            } catch (NullPointerException npe) {
                mainPanel.displayErrorMessage("NullPointerException: " + npe.getMessage());
                log.error("NullPointerException: ", npe);
            } catch (IndexOutOfBoundsException ioe) {
                mainPanel.displayErrorMessage("IndexOutOfBoundsException");
                log.error("IndexOutOfBoundsException: ", ioe);
            } catch (NoSuchElementException nse) {
                mainPanel.displayErrorMessage(nse.getMessage());
                log.error("NoSuchElementException: ", nse);
            } catch (IOException io) {
                mainPanel.displayErrorMessage(io.getMessage());
                log.error("IOException: ", io);
            } catch (Exception ex) {
                mainPanel.displayErrorMessage(ex.getMessage());
                log.error("Exception: ", ex);
            }
        }

        @Override
        public void windowClosing(WindowEvent event) {
            if (WindowEvent.WINDOW_CLOSING == event.getID()) {
                try {
                    serverDialog.sendXMLToServer(XMLParse.parseRequestToXML("close"));
                } catch (JAXBException jaxb) {
                    mainPanel.displayErrorMessage("Parse exception");
                    log.error("JAXBException: " + jaxb);
                }
                log.info("close");
                serverDialog.close();
                notificator.setWork(false);
                mainPanel.setVisible(false);
                mainPanel.dispose();
                System.exit(0);
            }
        }
    }
}




