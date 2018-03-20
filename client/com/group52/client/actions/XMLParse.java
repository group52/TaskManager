package com.group52.client.actions;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XMLParse {

    private static Client client;

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
        Client(){}

        Client(String login, String password, int id) {
            setLogin(login);
            setPassword(password);
            setId(id);
        }

    }

    @XmlRootElement (name = "socket")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class Socket {

        @XmlElement
        Client client;

        @XmlElement
        String action;

        @XmlElement
        int code;

        @XmlElement
        String status;

        @XmlElement(name = "task")
        ArrayList<XMLParse.Task> tasks;

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

        Socket(){}

        Socket(Client client, String action){
            setClient(client);
            setAction(action);
            tasks = new ArrayList<Task>();
        }

        void addTask(XMLParse.Task task){
            tasks.add(task);
        }
    }

    @XmlRootElement (name = "task")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class Task {
        @XmlAttribute(name = "title")
        String title;

        @XmlAttribute(name = "description")
        String description;

        @XmlAttribute(name = "time")
        long time;

        @XmlAttribute(name = "start")
        long start;

        @XmlAttribute(name = "end")
        long end;

        @XmlAttribute(name = "interval")
        int interval;

        @XmlAttribute(name = "active")
        boolean active;

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

        Task(){}

        Task(String title, String description, long time, long start, long end, int interval, boolean active) {
            setTitle(title);
            setDescription(description);
            setTime(time);
            setStart(start);
            setEnd(end);
            setInterval(interval);
            setActive(active);
        }
    }

    private static Marshaller createMarshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(XMLParse.Socket.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders",
                "\n<!DOCTYPE socket SYSTEM  \"xml\\client.dtd\">");
        return marshaller;
    }

    private static Unmarshaller createUnmarshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(XMLParse.Socket.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        //unmarshaller.setProperty(javax.xml.XMLConstants.ACCESS_EXTERNAL_DTD, Boolean.TRUE);
        return unmarshaller;
    }

    public static void createClient(String login, String password, int id) {
        client = new Client(login,password, id);
    }

    public static void setId(int id) {
        client.setId(id);
    }

    public static File parseRequestToXML(String request) throws JAXBException {
        File file = new File("xml/" + request + ".xml");
        Socket socket = new Socket(client, request);
        createMarshaller().marshal(socket, file);
        return file;
    }

    public static File parseTaskToXML(String request, String title, String description, long time,
                                      long start, long end, int interval, boolean active) throws JAXBException {
        File file = new File("xml/" + request + ".xml");
        Socket socket = new Socket(client, request);
        socket.addTask(new Task(title,description,time,start,end,interval,active));
        createMarshaller().marshal(socket, file);
        return file;
    }

    public static String getActionFromXML(File file) throws JAXBException {
        XMLParse.Socket socket = (XMLParse.Socket) createUnmarshaller().unmarshal(file);
        return socket.getAction();
    }

    public static int getCodeFromXML(File file) throws JAXBException {
        XMLParse.Socket socket = (XMLParse.Socket) createUnmarshaller().unmarshal(file);
        return socket.getCode();
    }

    public static String getStatusFromXML(File file) throws JAXBException {
        XMLParse.Socket socket = (XMLParse.Socket) createUnmarshaller().unmarshal(file);
        return socket.getStatus();
    }

    public static int getUserIdFromXML(File file) throws JAXBException {
        XMLParse.Socket socket = (XMLParse.Socket) createUnmarshaller().unmarshal(file);
        return socket.getClient().getId();
    }

    public static String getTaskFromXML(File file) throws JAXBException {
        XMLParse.Socket socket = (XMLParse.Socket) createUnmarshaller().unmarshal(file);
        List<XMLParse.Task> tasks = socket.getTasks();
        StringBuilder sb = new StringBuilder();
        for (XMLParse.Task task: tasks) {
            sb.append(task.getTitle() + ":");
            if (task.getInterval() == 0) {
                sb.append(" time: " + new Date(task.getTime()));
            }
            else {
                sb.append(" start: " + new Date(task.getStart()));
                sb.append(" end: " + new Date(task.getEnd()));
                sb.append(" interval: " + task.getInterval());
            }
            if (task.isActive()) sb.append(" (active) ");
            else sb.append(" (inactive) ");
            sb.append("\n");
            sb.append("Description: ");
            sb.append(task.getDescription());
            sb.append("\n");
        }
        return sb.toString();
    }
}