package com.group52.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
                activeClient = false;
            }
            socket.close();
        }
        catch(Exception e)
        {

        } 
        
    }

    /** Send the @param file using some @param OutputStream
    @param f is the file for send 
    @param os is the OutputStream for sending to the client */
    public static void sendFile(final OutputStream os, final File f) {      
        
        try (BufferedOutputStream bos = new BufferedOutputStream(os)){
            FileInputStream fis = new FileInputStream(f.getPath());
    
            int length;
            while((length = fis.read()) != -1) {           
                bos.write(length);                
            }
            fis.close();

        } catch (FileNotFoundException e) {
            Model.sendServerException(404,"Not Found");
        } catch (Exception e) {
            Model.sendServerException(500,"Internal Server Error");
        }
            
    }
    
    /** Receive the @return file using some @param InputStream  
    @param is is the InputStream for receive 
    @return f is the file from the client */
    private static File recieveFile(InputStream is) throws Exception {
        File f = new File("" + is.hashCode() + ".xml" );
        try (BufferedInputStream bis = new BufferedInputStream(is)) {            
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);

            int length;
            while((length = bis.read()) != -1) {           
                fos.write(length);                
            }            
            fos.flush();
            fos.close();            
            	
        } catch (IOException e) {
            Model.sendServerException(500,"Internal Server Error");
        }
        return f;
    }

}
