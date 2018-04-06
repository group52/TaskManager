package com.group52.client.actions;

import com.group52.client.view.SocketAddress;
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

        String socket = readSocketFromFile();
        if (socket != null) {
            try {
                String ip = socket.substring(0, socket.indexOf(':'));
                int port = Integer.parseInt(socket.substring(socket.indexOf(':')+ 1));
                new Handler(mainPanel, new ServerDialog(ip, port), notificator);
            } catch (IOException ioe) {
                mainPanel.displayErrorMessage(ioe.getMessage());
                log.error("InputOutput exceptions: ", ioe);
                createSocketForm(mainPanel, notificator);
            }
        } else {
            createSocketForm(mainPanel, notificator);
        }
    }

    public static void createSocketForm(MainPanel mainPanel, Notificator notificator) {
        SocketAddress socketAddress = new SocketAddress();
        socketAddress.open();
        socketAddress.addListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    if (socketAddress.okButton.equals(event.getSource())) {
                        socketAddress.readSocketAddress();
                        socketAddress.close();
                        String socket = socketAddress.getSocketAddress();
                        writeSocketToFile(socket);
                        String ip = socket.substring(0, socket.indexOf(':'));
                        int port = Integer.parseInt(socket.substring(socket.indexOf(':')+ 1));
                        new Handler(mainPanel, new ServerDialog(ip, port), notificator);
                        log.info("Changing server IP address to " + socket);
                    }
                    if (socketAddress.quitButton.equals(event.getSource())) {
                        log.info("Quit");
                        System.exit(1);
                    }
                } catch (NullPointerException npe) {
                    mainPanel.displayErrorMessage(npe.getMessage());
                    log.error("NullPointerException", npe);
                    socketAddress.open();
                } catch (IOException e) {
                    mainPanel.displayErrorMessage(e.getMessage());
                    log.error("IOException", e);
                    socketAddress.open();
                }
            }
        });
    }

    private static String readSocketFromFile() {
        String socket = null;
        Scanner in = null;
        try {
            in = new Scanner(new FileReader("socket.txt"));
            while(in.hasNext()) {
                socket = in.next();
            }
            in.close();
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException", e);
            new File("socket.txt");
        }
        return socket;
    }

    public static void writeSocketToFile(String socket) {
        try (FileWriter writer = new FileWriter("socket.txt")){
            writer.write(socket);
        } catch (IOException e) {
            log.error("IOException", e);
        }
    }
}
