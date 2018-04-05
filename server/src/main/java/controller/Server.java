package controller;

import model.ArrayTaskList;
import model.Client;
import model.Model;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import model.Task;
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
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())))
        {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            boolean activeClient = true;
            String ask;

            ask = recieveFile(in);

            if ("".equals(ask)) {
                activeClient = false;
                out.close();
                socket.close();
            }

            while (activeClient) {

                activeClient = activeClient(ask);

                if (activeClient) {
                    sendFile(out, doWork(ask));
                    sleep(500);
                    ask = recieveFile(in);
                }
                else {
                    out.close();
                    socket.close();
                    break;
                }
            }
        } catch(IOException ioe) {
            log.error("InputOutput exception: " + ioe);
        } catch (InterruptedException e) {
            log.error("InterruptedException: " + e);
        }
    }

    /** Send the @param file using some @param OutputStream
    @param s is the string for send
    @param output is the OutputStream for sending to the client */
    public void sendFile(PrintWriter output, String s) {

        s = s.replaceAll("\n","");
        output.println(s);
        log.debug("send xml" + s);

    }
    
    /** Receive the @return file using some @param InputStream  
    @param input is the input for receive
    @return the String from the client */
    private String recieveFile(BufferedReader input) throws IOException {

        String messageFromStream = "";
        try {
            messageFromStream = input.readLine();
            log.debug("get xml" + messageFromStream);
        } catch (IOException ioe) {
            socket.close();
            log.error("InputOutput exception: " + ioe);
        }
        return messageFromStream;
    }


    /** Ummarshaling the ask file from client and give the action for controller work
     @param s is the ask from client
     @return action type for controller work */
    public String getCommandType(String s)
    {
        return s.substring(s.indexOf(">",s.indexOf("<action")) + 1, s.indexOf("<",s.indexOf("<action>") + 1 ));
    }

    /** Check if the client want to finish the session
     @param recFile is the file from the client
     @return b is "true" if ok */
    public boolean activeClient(String recFile) {
        char status = getCommandType(recFile).charAt(0);

        switch (status) {
            case 'c': // "close"
                return false;
            default:
                break;
        }

        return true;
    }

    /** Check the client "action" from client and do that on controller side
     @param s is the string from the client
     @return sendFile is the file to send to the client */
    public String doWork(String s) {

        Model model = new Model();
        String sendAnswer;
        char status = getCommandType(s).charAt(0);

        Client client = model.getClient(s);

        switch (status) {
            case 'o':   // "oneMoreUser"
                if (!model.findLogin(client)) {
                    model.changeClient(model.newClient(client));
                    sendAnswer = model.sendId(client);
                }
                else
                    sendAnswer = model.sendStatus(client, 415, "Already exist");
                break;
            case 'u': // "user"
                if (model.getAvtorization(client)) {
                    Client clientUser = model.getClientFromFile(client);
                    clientUser.setId(client.getId());
                    model.changeClient(clientUser);
                    sendAnswer = model.sendId(clientUser);
                } else
                    sendAnswer = model.sendStatus(client,404, "Not Found");
                break;
            case 'n': // "notification"
                if (model.checkAvtorization(client)) {
                    Client clientNotification = model.getClientFromFile(client);
                    sendAnswer = model.sendTasksByTime(clientNotification);
                }
                else
                    sendAnswer = model.sendStatus(client,401, "Unauthorized");
                break;
            case 'v': // "view"
                if (model.checkAvtorization(client)) {
                    Client clientView = model.getClientFromFile(client);
                    sendAnswer = model.sendTasks(clientView);
                }
                else
                    sendAnswer = model.sendStatus(client,401, "Unauthorized");
                break;
            case 'a': // "add"
                if (model.checkAvtorization(client)) {
                    Client clientAdd = model.getClientFromFile(client);
                    if (model.workAdd(clientAdd, model.getAddTask(s)))
                        sendAnswer = model.sendStatus(clientAdd, 201, "Created");
                    else
                        sendAnswer = model.sendStatus(client, 400, "Bad Request");
                }
                else
                    sendAnswer = model.sendStatus(client,401, "Unauthorized");
                break;
            case 'e': // "edit"
                if (model.checkAvtorization(client)) {
                    Client clientEdit = model.getClientFromFile(client);
                    Task task = model.getDeleteTask(s);
                    model.workDelete(clientEdit, task);
                    ArrayTaskList tasks = model.getAddTask(s);
                    tasks.remove(task);
                    if (model.workAdd(clientEdit, tasks))
                        sendAnswer = model.sendStatus(clientEdit, 202, "Accepted");
                    else
                        sendAnswer = model.sendStatus(client, 400, "Bad Request");
                } else
                    sendAnswer = model.sendStatus(client,401, "Unauthorized");
                break;
            case 'd': // "delete"
                if (model.checkAvtorization(client)) {
                    Client clientDelete = model.getClientFromFile(client);
                    if (model.workDelete(clientDelete, model.getDeleteTask(s)))
                        sendAnswer = model.sendStatus(clientDelete, 202, "Accepted");
                    else
                        sendAnswer = model.sendStatus(client, 400, "Bad Request");
                } else
                    sendAnswer = model.sendStatus(client,401, "Unauthorized");
                break;
            case 'c': // "close"
                if (model.checkAvtorization(client)) {
                    client.setId((int) System.currentTimeMillis());
                    if (model.newSessionClient(client))
                        sendAnswer = model.sendStatus(client,200, "OK");
                    else
                        sendAnswer = model.sendStatus(client,400, "Bad Request");
                } else
                    sendAnswer = model.sendStatus(client,401, "Unauthorized");
                break;
            default:
                sendAnswer = model.sendStatus(client,405, "Method Not Allowed");
                break;
        }
        return sendAnswer;
    }

}
