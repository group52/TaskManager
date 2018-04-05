package com.group52.client.actions;

import com.group52.client.view.IPAddress;
import com.group52.client.view.MainPanel;
import org.apache.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

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

        String ip = readIPFromFile();
        if (ip != null) {
            new Handler(mainPanel, new ServerDialog(ip), notificator);
        } else {
            IPAddress ipAddress = new IPAddress();
            ipAddress.open();
            ipAddress.addListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    try {
                        if (ipAddress.okButton.equals(event.getSource())) {
                            ipAddress.readServerIP();
                            ipAddress.close();
                            String ip = ipAddress.getIp();
                            writeIPToFile(ip);
                            new Handler(mainPanel, new ServerDialog(ip), notificator);
                        }
                        if (ipAddress.quitButton.equals(event.getSource())) {
                            log.info("Quit");
                            System.exit(1);
                        }
                    } catch (NullPointerException npe) {
                        mainPanel.displayErrorMessage(npe.getMessage());
                        log.error(npe);
                    }
                }
            });
        }
    }

    private static String readIPFromFile() {
        String ip = null;
        Scanner in = null;
        try {
            in = new Scanner(new FileReader("ip.txt"));
            while(in.hasNext()) {
                ip = in.next();
            }
            in.close();
        } catch (FileNotFoundException e) {
            log.error(e);
            new File("ip.txt");
        }
        return ip;
    }

    public static void writeIPToFile(String ip) {
        try (FileWriter writer = new FileWriter("ip.txt")){
            writer.write(ip);
        } catch (IOException e) {
            log.error(e);
        }
    }
}
