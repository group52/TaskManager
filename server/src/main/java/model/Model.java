package model;
import java.io.*;

/** class Model make the main work between client-controller conversatio */
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

    /** Add new client on controller side and set "session_id" to client
     @param client is the information about the client
     @return b is "true" if ok */
    public static boolean newClient(Client client) throws Exception {
        client.setId((int) System.currentTimeMillis());
        XMLParse.addClient(client);
        return true;
    }

    /** Change client information on controller side and set "session_id" to client
     @param client is the information about the client
     @return b is "true" if ok */
    public static boolean getAvtorization(Client client) throws Exception {
        client.setId((int) System.currentTimeMillis());
        if (XMLParse.newSessionClient(client)) {
            return true;
        }
        return false;
    }

    /** Check the client "session_id" from client with the controller information
     @param client is the information about the client
     @return b is "true" if ok */
    public static boolean checkAvtorization(Client client) throws Exception {
        return XMLParse.findClient(client);
    }

    /** Check the client "action" from client and do that on controller side
     @param s is the string from the client
     @return sendFile is the file to send to the client */
    public static String doWork(String s) throws Exception {

        String sendFile;
        char status = Model.getCommandType(s).charAt(0);

        File recFile = new File("ask.xml");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter( recFile))){
            bw.write(s);
        } catch (FileNotFoundException e) {
            //log.error("TaskIO.writeText() failed", e);
        } catch (IOException e) {
            //log.error("TaskIO.writeText() failed", e);
        }
        Client client = XMLParse.getClient(recFile);

        System.out.println(status);
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
                    sendFile = XMLParse.sendStatus(client, 415, "Already exist");
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

    /** Ummarshaling the ask file from client and give the action for controller work
     @param s is the ask from client
     @return action type for controller work */
    public static String getCommandType(String s) throws Exception
    {
        return s.substring(s.indexOf(">",s.indexOf("<action")) + 1, s.indexOf("<",s.indexOf("<action>") + 1 ));
    }

   /** Check if the client want to finish the session
    @param recFile is the file from the client
    @return b is "true" if ok */
    public static boolean activeClient(String recFile) throws Exception {
        char status = Model.getCommandType(recFile).charAt(0);

        switch (status) {
            case 'c': // "close"
                return false;
            default:
                break;
        }

        return true;
    }

    /** Send answer with controller problem
     @return sendFile is the file to send to the client
    public static File sendServerException(int code, String status) {
        File sendFile;
        try
        {
            sendFile = XMLParse.sendStatus(new Client(),code,status);
        }
        catch (Exception e) {
            sendFile = new File("ServerException.xml");
        }
        return sendFile;
    }*/

}