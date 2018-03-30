package model;
import java.io.*;
import org.apache.log4j.Logger;

/** class Model make the main work between client-controller conversatio */
public class Model {

    private Logger log = Logger.getLogger(XMLParse.class);
    private XMLParse xmlParse = new XMLParse();

    public Model() {
    }

    /** Add list of task to the client task list and change "clientLogin.xml"
     @param client is the information about the client
     @param tasks is the task list for adding
     @return b is "true" if ok */
    public boolean workAdd(Client client, ArrayTaskList tasks) {
        client.addArrayList(tasks);
        xmlParse.changeClient(client);
        return true;
    }

    /** Delete task in the client task list and change "clientLogin.xml"
     @param client is the information about the client
     @param task is the task for delete
     @return b is "true" if ok */
    public boolean workDelete(Client client, Task task) {
        if (client.deleteArrayList(task)) {
            xmlParse.changeClient(client);
            return true;
        }
        return false;
    }

    /** Add new client on controller side and set "session_id" to client
     @param client is the information about the client
     @return b is "true" if ok */
    public Client newClient(Client client) {
        client.setId((int) System.currentTimeMillis());
        xmlParse.addClient(client);
        return client;
    }

    /** Change client information on controller side and set "session_id" to client
     @param client is the information about the client
     @return b is "true" if ok */
    public boolean getAvtorization(Client client) {
        client.setId((int) System.currentTimeMillis());
        if (xmlParse.newSessionClient(client)) {
            return true;
        }
        return false;
    }

    /** Check the client "session_id" from client with the controller information
     @param client is the information about the client
     @return b is "true" if ok */
    public boolean checkAvtorization(Client client) {
        return xmlParse.findClient(client);
    }

    /** Check the client "action" from client and do that on controller side
     @param s is the string from the client
     @return sendFile is the file to send to the client */
    public String doWork(String s) {

        log.info("get xml model" + s);
        String sendAnswer;
        char status = getCommandType(s).charAt(0);

        Client client = xmlParse.getClient(s);

        System.out.println(status);
        switch (status) {
            case 'o':   // "oneMoreUser"
                if (!xmlParse.findLogin(client)) {
                   xmlParse.changeClient(newClient(client));
                   sendAnswer = xmlParse.sendId(client);
                }
                else
                    sendAnswer = xmlParse.sendStatus(client, 415, "Already exist");
                break;
            case 'u': // "user"
                if (getAvtorization(client)) {
                    Client clientUser = xmlParse.getClient(new File("" + client.getLogin() + ".xml"));
                    clientUser.setId(client.getId());
                    clientUser.setPassword(client.getPassword());
                    xmlParse.changeClient(clientUser);
                    sendAnswer = xmlParse.sendId(clientUser);
                } else
                    sendAnswer = xmlParse.sendStatus(client,404, "Not Found");
                break;
            case 'n': // "notification"
                if (checkAvtorization(client)) {
                    Client clientNotification = xmlParse.getClient(new File("" + client.getLogin() + ".xml"));
                    sendAnswer = xmlParse.sendTasksByTime(clientNotification);
                }
                else
                    sendAnswer = xmlParse.sendStatus(client,401, "Unauthorized");
                break;
            case 'v': // "view"
                if (checkAvtorization(client)) {
                    Client clientView = xmlParse.getClient(new File("" + client.getLogin() + ".xml"));
                    sendAnswer = xmlParse.sendTasks(clientView);
                }
                else
                    sendAnswer = xmlParse.sendStatus(client,401, "Unauthorized");
                break;
            case 'a': // "add"
                if (checkAvtorization(client)) {
                    Client clientAdd = xmlParse.getClient(new File("" + client.getLogin() + ".xml"));
                    if (workAdd(clientAdd, xmlParse.getAddTask(s)))
                        sendAnswer = xmlParse.sendStatus(clientAdd, 201, "Created");
                    else
                        sendAnswer = xmlParse.sendStatus(client, 400, "Bad Request");
                }
                else
                    sendAnswer = xmlParse.sendStatus(client,401, "Unauthorized");
                break;
            case 'e': // "edit"
                if (checkAvtorization(client)) {
                    Client clientEdit = xmlParse.getClient(new File("" + client.getLogin() + ".xml"));
                    Task task = xmlParse.getDeleteTask(s);
                    workDelete(clientEdit, task);
                    ArrayTaskList tasks = xmlParse.getAddTask(s);
                    tasks.remove(task);
                    if (workAdd(clientEdit, tasks))
                        sendAnswer = xmlParse.sendStatus(clientEdit, 202, "Accepted");
                    else
                        sendAnswer = xmlParse.sendStatus(client, 400, "Bad Request");
                } else
                    sendAnswer = xmlParse.sendStatus(client,401, "Unauthorized");
                break;
            case 'd': // "delete"
                if (checkAvtorization(client)) {
                    Client clientDelete = xmlParse.getClient(new File("" + client.getLogin() + ".xml"));
                    if (workDelete(clientDelete, xmlParse.getDeleteTask(s)))
                        sendAnswer = xmlParse.sendStatus(clientDelete, 202, "Accepted");
                    else
                        sendAnswer = xmlParse.sendStatus(client, 400, "Bad Request");
                } else
                    sendAnswer = xmlParse.sendStatus(client,401, "Unauthorized");
                break;
            case 'c': // "close"
                if (checkAvtorization(client)) {
                    client.setId((int) System.currentTimeMillis());
                    if (xmlParse.newSessionClient(client))
                        sendAnswer = xmlParse.sendStatus(client,200, "OK");
                    else
                        sendAnswer = xmlParse.sendStatus(client,400, "Bad Request");
                } else
                    sendAnswer = xmlParse.sendStatus(client,401, "Unauthorized");
                break;
            default:
                sendAnswer = xmlParse.sendStatus(client,405, "Method Not Allowed");
                break;
        }
        return sendAnswer;
    }

    /** Ummarshaling the ask file from client and give the action for controller work
     @param s is the ask from client
     @return action type for controller work */
    public String getCommandType(String s)
    {
        log.info("get xml model" + s);
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
