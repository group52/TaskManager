package com.group52.client.actions;

import com.group52.client.view.IPAddress;
import com.group52.client.view.MainPanel;
import org.apache.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * class Main with start method "void main"
 */
public class Main {
    private static Logger log = Logger.getLogger(Handler.class);
    /**
     * method where we start our project
     * @param args are arguments with String[] type
     * @see main
     */
    public static void main(String[] args) {
        log.info("Start");

        Notificator notificator = new Notificator();
        notificator.setWork(true);
        Thread notificationThread = new Thread(notificator);
        notificationThread.start();

        MainPanel mainPanel = new MainPanel();
        String ip = null; //ip here will be read from file in future release
        if (ip != null) {
            new Handler(mainPanel, new ServerDialog(ip), notificator);
        } else {
            IPAddress ipAddress = new IPAddress();
            ipAddress.open();
            ipAddress.addListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    try {
                        if (event.getSource().equals(ipAddress.okButton)) {
                            ipAddress.readServerIP();
                            ipAddress.close();
                            new Handler(mainPanel, new ServerDialog(ipAddress.getIp()), notificator);
                        }
                        if (event.getSource().equals(ipAddress.quitButton)) {
                            log.info("Quit");
                            System.exit(1);
                        }
                    } catch (IllegalArgumentException iae) {
                        mainPanel.displayErrorMessage(iae.getMessage());
                        log.error("IllegalArgumentException: ", iae);
                    }
                }
            });
        }
    }
}
