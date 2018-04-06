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
        return xmlParse.newSessionClient(client);
    }

    /** Check the client "session_id" from client with the controller information
     @param client is the information about the client
     @return b is "true" if ok */
    public boolean checkAvtorization(Client client) {
        return xmlParse.findClient(client);
    }

    /** Ummarshaling the ask file from client and give the information about client
     @param s is the ask file from client
     @return client information */
    public Client getClient(String s) {
        return xmlParse.getClient(s);
    }

    /** Ummarshaling the ask file from client and give the information about client
     @param client is the client
     @return client information */
    public Client getClientFromFile(Client client) {
        File clientFile = new File(new java.io.File("xml/" + client.getLogin() + ".xml").getAbsolutePath());
        return xmlParse.getClient(clientFile);
    }

    /**
     * Find only login in "controller.xml" on controller side
     *
     * @param client is the client information
     */
    public boolean findLogin(Client client) {
        return xmlParse.findLogin(client);
    }

    /**  Change information about the client in "login.xml" on controller side
     @param client is the client information */
    public void changeClient(Client client) {
        xmlParse.changeClient(client);
    }

    /** Marshaling the answer file for "autorization" action from client
     @param client is the information about the client
     @return file is the answer file for "autorization" action from client */
    public String sendId(Client client) {
        return xmlParse.sendId(client);
    }


    /** Marshaling the answer file for status after some action from client
     @param client is the information about the client
     @param code is the answer code to the client
     @param status is the answer to client
     @return file is the answer file for status after some action from client */
    public String sendStatus(Client client, int code, String status) {
        return xmlParse.sendStatus(client, code, status);
    }

    /** Marshaling the answer file for "notification" action from client
     @param client is the information about the client
     @return file is the answer file for "notification" action from client */
    public String sendTasksByTime(Client client) {
        return xmlParse.sendTasksByTime(client);
    }

    /** Marshaling the answer file for "view" action from client
     @param client is the information about the client
     @return file is the answer file for "view" action from client */
    public String sendTasks(Client client) {
        return xmlParse.sendTasks(client);
    }

    /** Ummarshaling the ask file from client and give the task for delete
     @param s is the ask from client
     @return task is the task for delete */
    public Task getDeleteTask(String s) {
        return xmlParse.getDeleteTask(s);
    }

    /** Ummarshaling the ask file from client and give the task list for adding
     @param s is the ask from client
     @return tasks is the task list for adding */
    public ArrayTaskList getAddTask(String s) {
        return xmlParse.getAddTask(s);
    }

    /**
     * Change session id of the client in "controller.xml" on controller side
     *
     * @param client is the client information
     */
    public boolean newSessionClient(Client client) {
        return xmlParse.newSessionClient(client);
    }
}
