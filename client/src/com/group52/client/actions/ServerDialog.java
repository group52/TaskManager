package com.group52.client.actions;

import org.apache.log4j.Logger;
import java.io.*;
import java.net.Socket;

/**
 * class ServerDialog is class for send and get xml files
 */
public class ServerDialog {

    private Logger log = Logger.getLogger(Handler.class);
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * creating server dialog constructor
     * @see ServerDialog
     * @param serverAddress is server address
     */
    public ServerDialog(String serverAddress)  {
        try {
            socket = new Socket(serverAddress, 7002);
           // socket = new Socket("127.0.0.1", 7002);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ioe) {
            log.error("InputOutput exception: ", ioe);
            close();
        }
    }

    /**
     * method where we send XML to server
     * @param s is string from XMLParse
     */
    public void sendXMLToServer(String s) {
        s = s.replaceAll("\n","");
        out.println(s);
        log.debug("send xml: " + s);
    }

    /**
     * method where we get response from server
     * @return in is input
     */
    public String getResponseFromServer() {
        String messageFromStream = "";
        try {
            messageFromStream = in.readLine();
            log.debug("get xml: " + messageFromStream);
            if (messageFromStream == null) log.debug("got null response");
        } catch (IOException ioe) {
            log.error("InputOutput exception: ", ioe);
            close();
        }
        return messageFromStream;
    }

    /**
     * method for close all streams and socket
     */
    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException ioe) {
            log.error("InputOutput exception: ", ioe);
        }
    }

}
