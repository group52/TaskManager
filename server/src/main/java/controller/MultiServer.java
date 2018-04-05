package controller; 

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

/** class MultiServer for save and give the information about cliet's tasks */
public class MultiServer {
    static final int PORT = 7002;

    private static Logger log = Logger.getLogger(MultiServer.class);

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
                    log.error(e);
                }
            }
        } catch (IOException ioe) {
            log.error(ioe);
        }
    }
}

