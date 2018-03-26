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
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            File recFile;
            boolean activeClient = true;

            while (activeClient) {
                recFile = Server.recieveFile(is);
                Server.sendFile(os, Model.doWork(recFile));
                activeClient = Model.activeClient(recFile);                
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
    @param f is the file for send 
    @param os is the OutputStream for sending to the client */
    public static void sendFile(OutputStream output, File file) throws IOException {

        byte[] byteArray = new byte[(int) file.length()];

        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);

        DataInputStream dis = new DataInputStream(bis);
        dis.readFully(byteArray, 0, byteArray.length);
        
        //Sending file data to the socket
        output.write(byteArray, 0, byteArray.length);
        output.flush();
        bis.close();
    }
    
    /** Receive the @return file using some @param InputStream  
    @param is is the InputStream for receive 
    @return f is the file from the client */
    private static File recieveFile(InputStream is) throws Exception {
        File f = new File("xml/" + is.hashCode() + ".xml" );
        OutputStream output = new FileOutputStream(f);
        
        try (BufferedReader input = new BufferedReader(new InputStreamReader(is))) {            

            String text;            
            while(!(text = input.readLine()).equals("</socket>")) {                
                output.write(text.getBytes());                
            }  
            output.write(text.getBytes());
            output.close();   
        } 
        return f;
    }

}
