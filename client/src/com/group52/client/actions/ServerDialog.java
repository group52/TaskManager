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
    private InputStream input;
    private OutputStream output;


    /**
     * creating server dialog constructor
     * @see ServerDialog
     * @param serverAddress is server address
     */
    public ServerDialog(String serverAddress) throws IOException {
        socket = new Socket(serverAddress, 7002);
        //socket = new Socket("127.0.0.1", 7002);
        input = socket.getInputStream();
        output = socket.getOutputStream();
    }

    /**
     * method where we send XML to server
     * @param file is input file
     */
    public void sendXMLToServer(File file) {
        try {
            sendFile(output, file);
        } catch (IOException e) {
            log.error("IO Exception: ", e);
            close();
        }
    }

    /**
     * method where we get response from server
     * @return file is output file
     */
    public File getResponseFromServer() {
        File file = null;
        try {
            file = getFile(input);
        } catch (IOException e) {
            log.error("IO Exception: ", e);
            close();
        }
        return file;
    }

    /**
     * method where we transform file to stream
     * @param file is input file
     * @param output is output stream
     * @throws IOException if get input/output mistake
     */
    private static void sendFile(OutputStream output, File file) throws IOException {
    
        byte[] byteArray = new byte[(int) file.length()];

        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);

        DataInputStream dis = new DataInputStream(bis);
        dis.readFully(byteArray, 0, byteArray.length);

        //Sending file size to the socket
        //DataOutputStream dos = new DataOutputStream(output);
        //dos.writeLong(byteArray.length);
        //dos.write(byteArray, 0, byteArray.length);
        //dos.flush();

        //Sending file data to the socket
        output.write(byteArray, 0, byteArray.length);
        output.flush();
        bis.close();
    }

    /**
     * method where we transform stream to file
     * @param is is input stream
     * @throws IOException if get input/output mistake
     * @return file is output file
     */
    private static File getFile(InputStream is) throws IOException {
        
        File f = new File("xml/answer.xml");
        OutputStream output = new FileOutputStream(f);
        
        try (BufferedReader input = new BufferedReader(new InputStreamReader(is))) {            

            String text;            
            while(!(text = input.readLine()).equals("</socket>")) {                
                output.write(text.getBytes());                
            }  
            output.write(text.getBytes());
            output.close();            
            
            
        } catch (IOException e) {            
            e.printStackTrace();
            output.close();
            return f;
        }
        
        return f;
    }

    /**
     * method for close all streams and socket
     */
    public void close() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            log.error("IO Exception: ", e);
        }
    }

}
