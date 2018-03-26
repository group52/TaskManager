package controller; 

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/** class MultiServer for save and give the information about cliet's tasks */
public class MultiServer {
    static final int PORT = 7002;

    /** Start and close Server. Open information channel for each client */
    public static void main(String[] args) throws IOException {
        
        try (ServerSocket s = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = s.accept();
                try {
                    new Server(socket);
                }
                catch (Exception e) {
                    socket.close();
                }
            }
        }        
    }
}

