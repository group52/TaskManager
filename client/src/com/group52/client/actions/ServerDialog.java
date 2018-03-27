package com.group52.client.actions;

import org.apache.log4j.Logger;
import java.io.*;
import java.net.Socket;
import java.nio.CharBuffer;

/**
 * class ServerDialog is class for send and get xml files
 */
public class ServerDialog {

    private Logger log = Logger.getLogger(Handler.class);
    private Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;


    /**
     * creating server dialog constructor
     * @see ServerDialog
     * @param serverAddress is server address
     */
    public ServerDialog(String serverAddress) throws IOException  {
        socket = new Socket(serverAddress, 7002);
        //socket = new Socket("127.0.0.1", 7002);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * method where we send XML to server
     * @param br is stream from XMLParse
     */
    public static void sendXMLToServer(BufferedReader br) {
        out.println(br);
    }

    /**
     * method where we get response from server
     * @return in is input stream
     */
    public static BufferedWriter getResponseFromServer() throws IOException {
        return new BufferedWriter() = in.readLine();
    }

    /**
     * method for close all streams and socket
     */
    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            log.error("IO Exception: ", e);
        }
    }

}
