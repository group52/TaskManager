package com.group52.model;
import java.io.File;

/** class Model make the main work between client-server conversatio */
public class Model {

    /** Add list of task to the client task list and change "clientLogin.xml"
     @param client is the information about the client
     @param tasks is the task list for adding
     @return b is "true" if ok */
    public static boolean workAdd(Client client, ArrayTaskList tasks) throws Exception {
        client.addArrayList(tasks);
        XMLParse.changeClient(client);
        return true;
    }

    /** Delete task in the client task list and change "clientLogin.xml"
     @param client is the information about the client
     @param task is the task for delete
     @return b is "true" if ok */
    public static boolean workDelete(Client client, Task task) throws Exception {
        if (client.deleteArrayList(task)) {
            XMLParse.changeClient(client);
            return true;
        }
        return false;
    }

    /** Add new client on server side and set "session_id" to client
     @param client is the information about the client
     @return b is "true" if ok */
    public static boolean newClient(Client client) throws Exception {
        client.setId((int) System.currentTimeMillis());
        XMLParse.addClient(client);
        return true;
    }

    /** Change client information on server side and set "session_id" to client
     @param client is the information about the client
     @return b is "true" if ok */
    public static boolean getAvtorization(Client client) throws Exception {
        client.setId((int) System.currentTimeMillis());
        if (XMLParse.newSessionClient(client)) {
            return true;
        }
        return false;
    }

    /** Check the client "session_id" from client with the server information
     @param client is the information about the client
     @return b is "true" if ok */
    public static boolean checkAvtorization(Client client) throws Exception {
        return XMLParse.findClient(client);
    }

    /** Check the client "action" from client and do that on server side
     @param recFile is the file from the client
     @return sendFile is the file to send to the client */
    public static File doWork(File recFile) throws Exception {

        File sendFile;
        char status = XMLParse.getCommandType(recFile).charAt(0);
        Client client = XMLParse.getClient(recFile);


        switch (status) {
            case 'o':   // "oneMoreUser"
                if (!XMLParse.findLogin(client)) {                    
                    if (Model.newClient(client)) {
                        XMLParse.changeClient(client);
                        sendFile = XMLParse.sendId(client);
                    }
                    else
                        sendFile = XMLParse.sendStatus(client, 400, "Bad Request");
                }
                else
                    sendFile = XMLParse.sendStatus(client,415, "Already exist");
                break;
            case 'u': // "user"
                if (Model.getAvtorization(client)) {
                    Client clientUser = XMLParse.getClient(new File("" + client.getLogin() + ".xml"));
                    clientUser.setId(client.getId());
                    clientUser.setPassword(client.getPassword());
                    XMLParse.changeClient(clientUser);
                    sendFile = XMLParse.sendId(clientUser);
                } else
                    sendFile = XMLParse.sendStatus(client,404, "Not Found");
                break;
            case 'n': // "notification"
                if (Model.checkAvtorization(client)) {
                    Client clientNotification = XMLParse.getClient(new File("" + client.getLogin() + ".xml"));
                    sendFile = XMLParse.sendTasksByTime(clientNotification);
                }
                else
                    sendFile = XMLParse.sendStatus(client,401, "Unauthorized");
                break;
            case 'v': // "view"
                if (Model.checkAvtorization(client)) {
                    Client clientView = XMLParse.getClient(new File("" + client.getLogin() + ".xml"));
                    sendFile = XMLParse.sendTasks(clientView);
                }
                else
                    sendFile = XMLParse.sendStatus(client,401, "Unauthorized");
                break;
            case 'a': // "add"
                if (Model.checkAvtorization(client)) {
                    Client clientAdd = XMLParse.getClient(new File("" + client.getLogin() + ".xml"));
                    if (Model.workAdd(clientAdd, XMLParse.getAddTask(recFile)))
                        sendFile = XMLParse.sendStatus(clientAdd, 201, "Created");
                    else
                        sendFile = XMLParse.sendStatus(client, 400, "Bad Request");
                }
                else
                    sendFile = XMLParse.sendStatus(client,401, "Unauthorized");
                break;
            case 'e': // "edit"
                if (Model.checkAvtorization(client)) {
                    Client clientEdit = XMLParse.getClient(new File("" + client.getLogin() + ".xml"));
                    Task task = XMLParse.getDeleteTask(recFile);
                    Model.workDelete(clientEdit, task);
                    ArrayTaskList tasks = XMLParse.getAddTask(recFile);
                    tasks.remove(task);
                    if (Model.workAdd(clientEdit, tasks))
                        sendFile = XMLParse.sendStatus(clientEdit, 202, "Accepted");
                    else
                        sendFile = XMLParse.sendStatus(client, 400, "Bad Request");
                } else
                    sendFile = XMLParse.sendStatus(client,401, "Unauthorized");
                break;
            case 'd': // "delete"
                if (Model.checkAvtorization(client)) {
                    Client clientDelete = XMLParse.getClient(new File("" + client.getLogin() + ".xml"));
                    if (Model.workDelete(clientDelete, XMLParse.getDeleteTask(recFile)))
                        sendFile = XMLParse.sendStatus(clientDelete, 202, "Accepted");
                    else
                        sendFile = XMLParse.sendStatus(client, 400, "Bad Request");
                } else
                    sendFile = XMLParse.sendStatus(client,401, "Unauthorized");
                break;
            case 'c': // "close"
                if (Model.checkAvtorization(client)) {
                    client.setId((int) System.currentTimeMillis());
                    if (XMLParse.newSessionClient(client))
                        sendFile = XMLParse.sendStatus(client,200, "OK");
                    else
                        sendFile = XMLParse.sendStatus(client,400, "Bad Request");
                } else
                    sendFile = XMLParse.sendStatus(client,401, "Unauthorized");
                break;
            default:
                sendFile = XMLParse.sendStatus(client,405, "Method Not Allowed");
                break;
        }
        return sendFile;
    }

   /** Check if the client want to finish the session
    @param recFile is the file from the client
    @return b is "true" if ok */
    public static boolean activeClient(File recFile) throws Exception {
        char status = XMLParse.getCommandType(recFile).charAt(0);

        switch (status) {
            case 'c': // "close"
                return false;
            default:
                break;
        }

        return true;
    }

    /** Send answer with server problem
     @return sendFile is the file to send to the client */
    public static File sendServerException(int code, String status) {
        File sendFile;
        try
        {
            sendFile = XMLParse.sendStatus(new Client(),code, status);
        }
        catch (Exception e) {
            sendFile = new File("ServerException.xml");
        }
        return sendFile;
    }

}
