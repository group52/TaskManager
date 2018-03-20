package com.group52.client.actions;
import com.group52.client.view.*;
import com.group52.client.view.Calendar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import org.apache.log4j.*;

import javax.xml.bind.JAXBException;

public class Handler {

    private Logger log = Logger.getLogger(Handler.class);
    private MainPanel mainPanel;
    private ServerDialog serverDialog;

    public Handler(MainPanel mainPanel, ServerDialog serverDialog) {
        this.mainPanel = mainPanel;
        this.serverDialog = serverDialog;
        new Handler.Listener();
    }

    private void showTaskList () {
        try {
            serverDialog.sendXMLToServer(XMLParse.parseRequestToXML("view"));
            mainPanel.showTaskList(XMLParse.getTaskFromXML(serverDialog.getResponseFromServer()));
        } catch (JAXBException e) {
            mainPanel.displayErrorMessage("Parse exception");
            log.error(e);
            e.printStackTrace();
        }

    }

    private void editTasksToComboBox () { }

    public class Listener implements ActionListener {

        private WelcomeForm welcomeForm = new WelcomeForm();
        private SignUpForm signUpForm = new SignUpForm();
        private SignInForm signInForm = new SignInForm();
        private AddTaskForm unrepeatableTaskForm = new AddTaskForm("Unrepeatable");
        private AddTaskForm repeatableTaskForm = new AddTaskForm("Repeatable");

        public Listener() {
            Listener listener = this;
            mainPanel.addListener(listener);
            welcomeForm.addListener(listener);
            signUpForm.addListener(listener);
            signInForm.addListener(listener);
            unrepeatableTaskForm.addListener(listener);
            repeatableTaskForm.addListener(listener);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                if (e.getSource().equals(welcomeForm.signUpButton)) signUpForm.open();
                if (e.getSource().equals(signUpForm.cancelButton)) signUpForm.close();

                if (e.getSource().equals(welcomeForm.signInButton)) signInForm.open();
                if (e.getSource().equals(signInForm.cancelButton)) signInForm.close();

                if (e.getSource().equals(mainPanel.unrepeatableTaskFormButton)) unrepeatableTaskForm.open();
                if (e.getSource().equals(unrepeatableTaskForm.cancelButton)) unrepeatableTaskForm.close();

                if (e.getSource().equals(mainPanel.repeatableTaskFormButton)) repeatableTaskForm.open();
                if (e.getSource().equals(repeatableTaskForm.cancelButton)) repeatableTaskForm.close();

                if (e.getSource().equals(signUpForm.confirmButton)) {
                    String login = signUpForm.getLogin();
                    String password =  signUpForm.getPassword();
                    String repeatedPassword = signUpForm.getRepeatedPassword();

                    if (!password.equals(repeatedPassword))
                        mainPanel.displayMessage("Password false");
                    else XMLParse.createClient(login, password, 0);

                    serverDialog.sendXMLToServer(XMLParse.parseRequestToXML("oneMoreUser"));
                   // int code = XMLParse.getCodeFromXML(serverDialog.getResponseFromServer());
                    //if (code == 200) {
                        mainPanel.displayMessage("Successful");
                        signUpForm.close();
                        welcomeForm.close();
                        showTaskList();
                        mainPanel.open();
                   // }
                }

                if (e.getSource().equals(signInForm.confirmButton)) {
                    String login = signInForm.getLogin();
                    String password =  signInForm.getPassword();
                    XMLParse.createClient(login, password, 0);
                    serverDialog.sendXMLToServer(XMLParse.parseRequestToXML("user"));
                    XMLParse.createClient(login, password, 0);

                    //int code = XMLParse.getCodeFromXML(serverDialog.getResponseFromServer());
                   // if (code == 200) {
                        mainPanel.displayMessage("Avtorized");
                        signInForm.close();
                        welcomeForm.close();
                        showTaskList();
                        mainPanel.open();
                    //}
                }
                if (e.getSource().equals(unrepeatableTaskForm.unrepeatableTaskButton)) {
                    String title = unrepeatableTaskForm.getTitle();
                    String description = unrepeatableTaskForm.getDescription();
                    long time = unrepeatableTaskForm.getStartTime();
                    boolean active = unrepeatableTaskForm.activeBox.isSelected();
                    serverDialog.sendXMLToServer(XMLParse.parseTaskToXML("add",
                            title, description, time,0,0,0, active));
                    showTaskList();
                    unrepeatableTaskForm.close();
                }

                if (e.getSource().equals(repeatableTaskForm.repeatableTaskButton)) {
                    String title = repeatableTaskForm.getTitle();
                    String description = repeatableTaskForm.getDescription();
                    long start = repeatableTaskForm.getStartTime();
                    long end = repeatableTaskForm.getEndTime();
                    int interval = repeatableTaskForm.getInterval();
                    boolean active = repeatableTaskForm.activeBox.isSelected();
                    serverDialog.sendXMLToServer(XMLParse.parseTaskToXML("add",
                            title, description, 0,start,end,interval, active));
                    showTaskList();
                    repeatableTaskForm.close();
                }

                if (e.getSource().equals(mainPanel.calendarFormButton)) {
                    new Calendar(true);
                }
                if (e.getSource().equals(mainPanel.exitButton)) {
                    log.info("Exit");
                    serverDialog.sendXMLToServer(XMLParse.parseRequestToXML("close"));
                    //serverDialog.close();
                    mainPanel.setVisible(false);
                    mainPanel.dispose();
                    System.exit(0);
                    //Main.main(new String[]{});
                }
            } catch (IllegalArgumentException iae) {
                mainPanel.displayErrorMessage(iae.getMessage());
                log.error("IllegalArgumentException: ", iae);
            } catch (NullPointerException npe) {
                mainPanel.displayErrorMessage(npe.getMessage());
                log.error("NullPointerException: ", npe);
                npe.printStackTrace();
            } catch (IndexOutOfBoundsException ioe) {
                mainPanel.displayErrorMessage(ioe.getMessage());
                log.error("IndexOutOfBoundsException: ", ioe);
            } catch (NoSuchElementException nse) {
                mainPanel.displayErrorMessage(nse.getMessage());
                log.error("NoSuchElementException: ", nse);
            } catch (Exception ex) {
                mainPanel.displayErrorMessage(ex.getMessage());
                log.error("Exception: ", ex);
                ex.printStackTrace();
            }
        }
    }
}




