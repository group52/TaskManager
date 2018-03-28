package controller; 

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//import org.apache.log4j.Logger;

/** class MultiServer for save and give the information about cliet's tasks */
public class MultiServer {
    static final int PORT = 7002;

    //private Logger log = Logger.getLogger(MultiServer.class);

    /** Start and close Server. Open information channel for each client */
    public static void main(String[] args) {
        
        try (ServerSocket s = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = s.accept();
                try {
                    new Server(socket);
                }
                catch (Exception e) {
                    socket.close();
                    System.out.println(e.getMessage());
                }
            }
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

