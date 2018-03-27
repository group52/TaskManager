package com.group52.client.actions;

import com.group52.client.view.MainPanel;
import org.apache.log4j.Logger;
import java.io.IOException;

/**
 * class Main with start method "void main"
 */
public class Main {
    /**
     * method where we start our project
     * @param args are arguments with String[] type
     * @see main
     */
    public static void main(String[] args) {
        Logger log = Logger.getLogger(Handler.class);
        log.info("Start");

        Notificator notificator = new Notificator();
        Thread notificationThread = new Thread(notificator);
        notificationThread.start();

        MainPanel mainPanel = new MainPanel();

        ServerDialog serverDialog = new ServerDialog(mainPanel.getServerAddress());

        new Handler(mainPanel, serverDialog, notificator);
    }
}
