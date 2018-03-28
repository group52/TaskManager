package com.group52.client.actions;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * class XMLParse for parse xml files
 */
public class XMLParse {

    private static Client client;

    /**
     * inner class Client for construct login, password, user id fields
     */
    @XmlRootElement(name = "user")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class Client {

        @XmlAttribute(name = "login")
        String login;
        @XmlAttribute(name = "password")
        String password;
        @XmlElement
        int id;

        String getLogin() {
            return login;
        }

        void setLogin(String login) {
            this.login = login;
        }

        String getPassword() {
            return password;
        }

        void setPassword(String password) {
            this.password = password;
        }

        int getId() {
            return id;
        }

        void setId(int id) {
            this.id = id;
        }

        /**
         * empty constructor
         * @see Client
         */
        Client(){}

        /**
         * creating client constructor
         * @see Client
         * @param login of user
         * @param password of user
         * @param id of user
         */
        Client(String login, String password, int id) {
            setLogin(login);
            setPassword(password);
            setId(id);
        }

    }

    /**
     * inner class Socket for construct client, action, code, status fields
     */
    @XmlRootElement (name = "socket")
    @XmlAccessorType(XmlAccessType.FIELD)
    static class Socket {

        @XmlElement
        private Client client;

        @XmlElement
        private String action;

        @XmlElement
        private int code;

        @XmlElement
        private String status;

        @XmlElement(name = "task")
        private ArrayList<XMLParse.Task> tasks;

        List<Task> getTasks() {
            return tasks;
        }

        void setTasks(ArrayList<XMLParse.Task> tasks) {
            this.tasks = tasks;
        }

        Client getClient() {
            return client;
        }

        void setClient(Client client) {
            this.client = client;
        }

        String getAction() {
            return action;
        }

        void setAction(String action) {
            this.action = action;
        }

        int getCode() {
            return code;
        }

        void setCode(int code) {
            this.code = code;
        }

        String getStatus() {
            return status;
        }

        void setStatus(String status) {
            this.status = status;
        }

        /**
         * empty constructor
         * @see Socket
         */
        Socket(){}

        /**
         * creating socket constructor
         * @see Socket
         * @param client is user
         * @param action is what we will do
         */
        Socket(Client client, String action) {
            setClient(client);
            setAction(action);
            tasks = new ArrayList<Task>();
        }

        void addTask(XMLParse.Task task){
            tasks.add(task);
        }
    }

    /**
     * inner class Task for construct tasks fields
     */
    @XmlRootElement (name = "task")
    @XmlAccessorType(XmlAccessType.FIELD)
    static class Task {
        @XmlAttribute(name = "title")
        private String title;

        @XmlAttribute(name = "description")
        private String description;

        @XmlAttribute(name = "time")
        private long time;

        @XmlAttribute(name = "start")
        private long start;

        @XmlAttribute(name = "end")
        private long end;

        @XmlAttribute(name = "interval")
        private int interval;

        @XmlAttribute(name = "active")
        private boolean active;

        String getTitle() {
            return title;
        }

        void setTitle(String title) {
            this.title = title;
        }

        String getDescription() {
            return description;
        }

        void setDescription(String description) {
            this.description = description;
        }

        long getTime() {
            return time;
        }

        void setTime(long time) {
            this.time = time;
        }

        long getStart() {
            return start;
        }

        void setStart(long start) {
            this.start = start;
        }

        long getEnd() {
            return end;
        }

        void setEnd(long end) {
            this.end = end;
        }

        int getInterval() {
            return interval;
        }

        void setInterval(int interval) {
            this.interval = interval;
        }

        boolean isActive() {
            return active;
        }

        void setActive(boolean active) {
            this.active = active;
        }

        /**
         * empty constructor
         * @see Task
         */
        Task(){}

        /**
         * creating task using second constructor
         * @see Task
         * @param title of task
         * @param description of task
         * @param time of task
         * @param start time of task
         * @param end time of task
         * @param interval of task
         * @param active of task (can be true or false)
         */
        Task(String title, String description, long time, long start, long end, int interval, boolean active) {
            setTitle(title);
            setDescription(description);
            setTime(time);
            setStart(start);
            setEnd(end);
            setInterval(interval);
            setActive(active);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(getTitle() + ":");
            if (getInterval() == 0) {
                sb.append(" time: " + new Date(getTime()));
            }
            else {
                sb.append(" start: " + new Date(getStart()));
                sb.append(" end: " + new Date(getEnd()));
                sb.append(" interval: " + getInterval());
            }
            if (isActive()) sb.append(" (active) ");

            else sb.append(" (inactive) ");
            sb.append(" Description: ");
            sb.append(getDescription());
            sb.append("\n");
            return sb.toString();
        }
    }

    /**
     * method where we create the marshaller
     * @throws JAXBException if JAXB parser has a problem
     * @return marshaller
     */
    private static Marshaller createMarshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(XMLParse.Socket.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        /*marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders",
                "\n<!DOCTYPE socket SYSTEM  \"client.dtd\">");*/
        return marshaller;
    }

    /**
     * method where we create the unmarshaller
     * @throws JAXBException if JAXB parser has a problem
     * @return unmarshaller
     */
    private static Unmarshaller createUnmarshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(XMLParse.Socket.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        //unmarshaller.setProperty(javax.xml.XMLConstants.ACCESS_EXTERNAL_DTD, Boolean.TRUE);
        return unmarshaller;
    }

    /**
     * method where we create the client
     * @param login of client
     * @param password of client
     * @param id of client
     */
    public static void createClient(String login, String password, int id) {
        client = new Client(login,password, id);
    }

    /**
     * method where we set client's id
     * @param id of client
     */
    public static void setId(int id) {
        client.setId(id);
    }

    /**
     * method where we parse request to XML
     * @param request for server
     * @return string xml
     */
    public static String parseRequestToXML(String request) throws JAXBException {
        Socket socket = new Socket(client, request);
        StringWriter sw = new StringWriter();
        createMarshaller().marshal(socket, sw);
        return sw.toString();
    }

    /**
     * method where we parse task to XML
     * @param request for server
     * @param title of task
     * @param description of task
     * @param time of task
     * @param start time of task
     * @param end time of task
     * @param interval of task
     * @param active of task (can be true or false)
     * @throws JAXBException if JAXB parser has a problem
     * @return string xml
     */
    public static String parseTaskToXML(String request, String title, String description, long time,
                                      long start, long end, int interval, boolean active) throws JAXBException {
        Socket socket = new Socket(client, request);
        socket.addTask(new Task(title,description,time,start,end,interval,active));
        StringWriter sw = new StringWriter();
        createMarshaller().marshal(socket, sw);
        return sw.toString();
    }

    /**
     * overflow method where we parse task to XML(for edit or notification)
     * @param request for server
     * @param oldTask old task
     * @param title of new task
     * @param description of new task
     * @param time of new task
     * @param start time of new task
     * @param end time of new task
     * @param interval of new task
     * @param active of new task (can be true or false)
     * @throws JAXBException if JAXB parser has a problem
     * @return string xml
     */
    public static String parseTaskToXML(String request, XMLParse.Task oldTask, String title, String description, long time,
                                      long start, long end, int interval, boolean active) throws JAXBException {
        Socket socket = new Socket(client, request);
        socket.addTask(oldTask);
        socket.addTask(new Task(title,description,time,start,end,interval,active));
        StringWriter sw = new StringWriter();
        createMarshaller().marshal(socket, sw);
        return sw.toString();
    }

    /**
     * method where we parse task to XML(for delete)
     * @param request for server
     * @param task is parsed task
     * @throws JAXBException if JAXB parser has a problem
     * @return string xml
     */
    public static String parseTaskToXML(String request, XMLParse.Task task) throws JAXBException {
        Socket socket = new Socket(client, request);
        socket.addTask(task);
        StringWriter sw = new StringWriter();
        createMarshaller().marshal(socket, sw);
        return sw.toString();
    }

    /**
     * method where we get action from XML
     * @param s is string(xml) from server
     * @throws JAXBException if JAXB parser has a problem
     * @return action
     */
    public static String getActionFromXML(String s) throws JAXBException {
        XMLParse.Socket socket = (XMLParse.Socket) createUnmarshaller().unmarshal(new StringReader(s));
        return socket.getAction();
    }

    /**
     * method where we get code from XML
     * @param s is string(xml) from server
     * @throws JAXBException if JAXB parser has a problem
     * @return code
     */
    public static int getCodeFromXML(String s) throws JAXBException {
        XMLParse.Socket socket = (XMLParse.Socket) createUnmarshaller().unmarshal(new StringReader(s));
        return socket.getCode();
    }

    /**
     * method where we get status from XML
     * @param s is string(xml) from server
     * @throws JAXBException if JAXB parser has a problem
     * @return status
     */
    public static String getStatusFromXML(String s) throws JAXBException {
        XMLParse.Socket socket = (XMLParse.Socket) createUnmarshaller().unmarshal(new StringReader(s));
        return socket.getStatus();
    }

    /**
     * method where we get user id from XML
     * @param s is string(xml) from server
     * @throws JAXBException if JAXB parser has a problem
     * @return user id
     */
    public static int getUserIdFromXML(String s) throws JAXBException {
        XMLParse.Socket socket = (XMLParse.Socket) createUnmarshaller().unmarshal(new StringReader(s));
        return socket.getClient().getId();
    }

    /**
     * method where we get tasks from XML for showing on main panel
     * @param s is string(xml) from server
     * @throws JAXBException if JAXB parser has a problem
     * @return tasks
     */
    public static String getTasksFromXML(String s) throws JAXBException {
        XMLParse.Socket socket = (XMLParse.Socket) createUnmarshaller().unmarshal(new StringReader(s));
        List<XMLParse.Task> tasks = socket.getTasks();
        StringBuilder sb = new StringBuilder();
        for (XMLParse.Task task: tasks) {
            sb.append(task.toString());
        }
        return sb.toString();
    }

    /**
     * method where we get task list from XML
     * @param s is string(xml) from server
     * @throws JAXBException if JAXB parser has a problem
     * @return task list
     */
    public static List<Task> getTasks(String s) throws JAXBException {
        XMLParse.Socket socket = (XMLParse.Socket) createUnmarshaller().unmarshal(new StringReader(s));
        return new ArrayList<>(socket.getTasks());
    }
}