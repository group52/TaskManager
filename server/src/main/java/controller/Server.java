package controller;

import model.Model;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;

/** class Server for work with one client(receive and save files) */
public class Server extends Thread {

    private Socket socket;
    private Logger log = Logger.getLogger(Server.class);

     /** Constructor for each client 
    @param socket is the socket for give and receive information */
    public Server(Socket socket)
    {
        this.socket = socket;
        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    /** Main work for each thread */
    public void run()
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new PrintWriter(socket.getOutputStream(), true));

            Model model = new Model();
            boolean activeClient = true;
            String ask;

            while (activeClient) {
                ask = recieveFile(in);
                sendFile(out, model.doWork(ask));
                activeClient = model.activeClient(ask);
            }            
        } catch(IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    /** Send the @param file using some @param OutputStream
    @param s is the string for send
    @param output is the OutputStream for sending to the client */
    public void sendFile(BufferedWriter output, String s) {

        try {
            output.write(s);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    /** Receive the @return file using some @param InputStream  
    @param input is the input for receive
    @return the String from the client */
    private String recieveFile(BufferedReader input) {

        String text = "";
        try {
            text = input.readLine();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return text;
    }

}
