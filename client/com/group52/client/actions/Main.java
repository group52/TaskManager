package com.group52.client.actions;

import com.group52.client.view.MainPanel;
import org.apache.log4j.Logger;

public class Main {
    public static void main(String[] args) {
        Logger log = Logger.getLogger(Handler.class);
        log.info("Start");

        MainPanel theView = new MainPanel();
        /*
        try {
            ServerDialog serverDialog = new ServerDialog(theView.getServerAddress());
            new Handler(theView, serverDialog);
        } catch (IOException e) {
            log.error("IO Exception: ", e);
        }
        */

        ServerDialog serverDialog = new ServerDialog();//for testing
        new Handler(theView, serverDialog);
    }
}
