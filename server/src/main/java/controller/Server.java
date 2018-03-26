package controller;

import model.Model;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/** class Server for work with one client(receive and save files) */
public class Server extends Thread {

    private Socket socket;

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

            boolean activeClient = true;
            String ask;

            while (activeClient) {
                ask = Server.recieveFile(in);
                Server.sendFile(out, Model.doWork(ask));
                activeClient = Model.activeClient(ask);
            }            
        }
        catch(Exception e)
        {
        }finally {
            try {
                socket.close();
            } catch (IOException e) {

            }
        }
    }

    /** Send the @param file using some @param OutputStream
    @param s is the string for send
    @param output is the OutputStream for sending to the client */
    public static void sendFile(BufferedWriter output, String s) throws IOException {

       output.write(s);
    }
    
    /** Receive the @return file using some @param InputStream  
    @param input is the input for receive
    @return the String from the client */
    private static String recieveFile(BufferedReader input) throws Exception {
        return input.readLine();
    }

}
