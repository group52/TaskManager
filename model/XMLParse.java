package com.group52.model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/** class XMLParse for parsing, marshaling and unmarshaling the *.xml files from the client to the server */
public class XMLParse {

    /** subclass Server for saving information in xml-format on the server side */
    @XmlRootElement (name = "server")
    @XmlAccessorType(XmlAccessType.FIELD)
    static class Server {
        
        @XmlElement(name = "client")
        private ArrayList<XMLParse.ServerClient> clients;

        /** Empty constructor without clients on the server */
        public Server(){
            clients = new ArrayList<XMLParse.ServerClient>();
        }

        /** Add client to the server
         @param client is the client for adding */
        public void add(XMLParse.ServerClient client){
            clients.add(client);
        }

        /** Add client to the server
         @return clients is the list of the clients */
        public List<XMLParse.ServerClient> getClients() {
            return clients;
        }
    }

    /** Subclass ServerClien give information about user for the server */
    @XmlRootElement(name = "client")
    @XmlAccessorType(XmlAccessType.FIELD)
    static class ServerClient {
        // поля
        @XmlAttribute(name = "login")
        String login;
        @XmlAttribute(name = "password")
        String password;
        @XmlElement(name = "session_id")
        int id;

        /** Return the login of the client
         @return login is the login of the client */
        public String getLogin() {
            return login;
        }

        /** Setup the login for the client
         @param login is the password for the client */
        public void setLogin(String login) {
            this.login = login;
        }

        /** Return the password of the client
         @return password is the password of the client */
        public String getPassword() {
            return password;
        }

        /** Setup the password for the client
         @param password is the password for the client */
        public void setPassword(String password) {
            this.password = password;
        }

        /** Return the session id of the client, which is new for each autorization
         @return id is the session id of the client */
        public int getId() {
            return id;
        }

        /** Setup the session id for the client
         @param id is the session id for the client */
        public void setId(int id) {
            this.id = id;
        }

        /** Empty constructor for the client */
        public ServerClient() {
        }

        /** Constructor for the client
         @param login is the login of the client
         @param password is the password of the client
         @param id is the session id of the client */
        public ServerClient(String login, String password, int id) {
            setLogin(login);
            setPassword(password);
            setId(id);
        }

    }

    /** subclass Socket for sending and receiving information in xml-format between the server and client */
    @XmlRootElement (name = "socket")
    @XmlAccessorType(XmlAccessType.FIELD)
    static class Socket {
    
        @XmlElement(name = "client")
        XMLParse.ServerClient serverClient;

        @XmlElement(name = "action")
        String action;

        @XmlElement(name = "code")
        int code;

        @XmlElement(name = "status")
        String status;
        
        @XmlElement(name = "task")
        private ArrayList<XMLParse.TaskClient> tasks;

        /** Return the tasks of the client
         @return tasks is the tasks of the client */
        public List<XMLParse.TaskClient> getTasks() {
            return tasks;
        }

        /** Setup the tasks to the task list
         @param tasks is the tasks of the task list */
        public void setTasks(ArrayList<XMLParse.TaskClient> tasks) {
            this.tasks = tasks;
        }

        /** Return the client information
         @return serverClient is the client information */
        public ServerClient getServerClient() {
            return serverClient;
        }

        /** Setup the client information
         @param serverClient is the client information */
        public void setServerClient(ServerClient serverClient) {
            this.serverClient = serverClient;
        }

        /** Return the action which the client want to do with the tasks list
         @return action is the action which the client want to do */
        public String getAction() {
            return action;
        }

        /** Setup the action of the server work
         @param action is the action of the server work */
        public void setAction(String action) {
            this.action = action;
        }

        /** Return the code of the problem with client information from server side
         @return code is the problem with client information */
        public int getCode() {
            return code;
        }

        /** Setup the code of the problem with client information from server side
         @param code is the code of the problem with client information */
        public void setCode(int code) {
            this.code = code;
        }

        /** Return the status of server answer ("true" or "false")
         @return status is the status of server answer */
        public String getStatus() {
            return status;
        }

        /** Setup the status of server answer ("true" or "false")
         @param status is the status of server answer */
        public void setStatus(String status) {
            this.status = status;
        }

        /** Empty constructor for the socket */
        public Socket(){
            serverClient = new ServerClient();
            action = "";
            code = 0;
            status = "";
            tasks = new ArrayList<XMLParse.TaskClient>();
        }

        /** Constructor for the one .xml file for send
         @param serverClient is the client information
         @param action is the action of the server work
         @param code is the code of the problem with client information
         @param status is the status of server answer */
        public Socket(XMLParse.ServerClient serverClient, String action, int code, String status){
            setServerClient(serverClient);
            setAction(action);
            setCode(code);
            setStatus(status);
            tasks = new ArrayList<XMLParse.TaskClient>();
        }

        /** Add task to the client task list
         @param task is the new task for addding */
        public void addTask(XMLParse.TaskClient task){
            tasks.add(task);
        }
    }

    /** subclass TaskClient is the task of the client */
    @XmlRootElement (name = "task")
    @XmlAccessorType(XmlAccessType.FIELD)
    static class TaskClient {
        @XmlAttribute(name = "title")
        String title;

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
        
        @XmlAttribute(name = "description")
        String description;

        /** Return the description of the task
         @return the description of the task */
        public String getDescription() {
            return description;
        }

        /** Setup the description of the task
         @param description is the description of the task */
        public void setDescription(String description) {
            this.description = description;
        }

        /** Return the title of the task
         @return the title of the task */
        public String getTitle() {
            return title;
        }

        /** Setup the title of the task
         @param title is the title of the task */
        public void setTitle(String title) {
            this.title = title;
        }

        /** Return the start time of the task
         @return the start time of the repeated task
         or time of the non repeated task */
        public long getTime() {
            return time;
        }

        /** Setup the time of the non repeated task
         and make non repeated task for repeated task
         @param time is the time of the non repeated task */
        public void setTime(long time) {
            this.time = time;
        }

        /** Return the same as method getTime()
         @return the start time of the repeated task
         or time of the non repeated task */
        public long getStart() {
            return start;
        }

        /** Setup the start of the task
         @param start is the start time of the task */
        public void setStart(long start) {
            this.start = start;
        }

        /** Return the end time of the repeated task
         or time of the non repeated task
         @return the end time of the repeated task
         or time of the non repeated task */
        public long getEnd() {
            return end;
        }

        /** Setup the end of the repeated task
         @param end is the end of repeated period */
        public void setEnd(long end) {
            this.end = end;
        }

        /** Return the time between two same repeated tasks
         or 0 for the non repeated task
         @return the time between two same repeated tasks
         or 0 for the non repeated task */
        public int getInterval() {
            return interval;
        }

        /** Setup the interval of the repeated task
         @param interval is the time between two same tasks  */
        public void setInterval(int interval) {
            this.interval = interval;
        }

        /** Return the activity of the task
         @return the activity of the task */
        public boolean isActive() {
            return active;
        }

        /** Setup the activity of the task
         @param active is the activity of the task */
        public void setActive(boolean active) {
            this.active = active;
        }

        /** Empty constructor for the task */
        public TaskClient() {
        }

        /** Constructor for the tasks
         @param title is the title of the task;
         @param start is the start time of the task
         @param end is the end of repeated period
         @param interval is the time between two same tasks
         @param description is the description of the task */
        public TaskClient(String title, long time, long start, long end, int interval, boolean active, String description) {
            this.title = title;
            this.time = time;
            this.start = start;
            this.end = end;
            this.interval = interval;
            this.active = active;
            this.description = description;
        }
    }

    /** Add the new client to "server.xml" on server side
    @param client is the new client for adding */
    public static void addClient(Client client) throws Exception {

        String fileName = "server.xml";
        File file = new File(fileName);

        JAXBContext jc = JAXBContext.newInstance(XMLParse.Server.class);
        Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        XMLParse.Server server = (XMLParse.Server) jaxbUnmarshaller.unmarshal(file);

        ServerClient serverClient = new ServerClient(client.getLogin(),Integer.toString(client.getPassword()),client.getId());
        server.add(serverClient);

        Marshaller jaxbMarshaller = jc.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(server,new File("server.xml"));
    }

    /** Change session id of the client in "server.xml" on server side
     @param client is the client information */
    public static boolean newSessionClient(Client client) throws Exception {

        String file = "server.xml";

        JAXBContext jc = JAXBContext.newInstance(XMLParse.Server.class);
        Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        XMLParse.Server server = (XMLParse.Server) jaxbUnmarshaller.unmarshal(new File(file));

        List<XMLParse.ServerClient> clientsList = server.getClients();

        for (XMLParse.ServerClient serverClient: clientsList) {
            if (serverClient.getLogin().equals(client.getLogin())
                    && (serverClient.getPassword()).equals(Integer.toString(client.getPassword()))) {
                serverClient.setId(client.getId());

                Marshaller jaxbMarshaller = jc.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                jaxbMarshaller.marshal(server,new File(file));
                return true;
            }
        }

        return false;
    }

    /**  Find only login in "server.xml" on server side
    @param client is the client information */
    public static boolean findLogin(Client client) throws Exception {

        String file = "server.xml";

        JAXBContext jc = JAXBContext.newInstance(XMLParse.Server.class);
        Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        XMLParse.Server server = (XMLParse.Server) jaxbUnmarshaller.unmarshal(new File(file));

        List<XMLParse.ServerClient> clientsList = server.getClients();

        for (XMLParse.ServerClient serverClient: clientsList) {
            if (serverClient.getLogin().equals(client.getLogin())) {
                return true;
            }
        }

        return false;
    }

    /**  Chack all information about the client in "server.xml" on server side
     @param client is the client information */
    public static boolean findClient(Client client) throws Exception {

        String file = "server.xml";

        JAXBContext jc = JAXBContext.newInstance(XMLParse.Server.class);
        Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        XMLParse.Server server = (XMLParse.Server) jaxbUnmarshaller.unmarshal(new File(file));

        List<XMLParse.ServerClient> clientsList = server.getClients();

        for (XMLParse.ServerClient serverClient: clientsList) {
            if (serverClient.getLogin().equals(client.getLogin())
                    && (serverClient.getPassword()).equals(Integer.toString(client.getPassword()))
                    && serverClient.getId() == client.getId()) {
                return true;
            }
        }

        return false;
    }

    /**  Change information about the client in "login.xml" on server side
     @param client is the client information */
    public static void changeClient(Client client) throws Exception
    {
        String filename = "" + client.getLogin() + ".xml";
        File file = new File(filename);
        XMLParse.ServerClient serverClient = new XMLParse.ServerClient(client.getLogin(),Integer.toString(client.getPassword()),client.getId());
        XMLParse.Socket socket = new XMLParse.Socket(serverClient, "", 200, "Ok");
        for (Task task : client.getArrayList()) {
            socket.addTask(new XMLParse.TaskClient(task.getTitle(),task.getTime(),task.getStartTime(),task.getEndTime(),task.getRepeatInterval(),task.isActive(),task.getDescription()));
        }

        JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
        Marshaller jaxbMarshaller = jc.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(socket,file);
    }

   /** Ummarshaling the ask file from client and give the action for server work
    @param file is the ask file from client
    @return action type for server work */
    public static String getCommandType(File file) throws Exception
    {
        JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
        Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        XMLParse.Socket socket = (XMLParse.Socket) jaxbUnmarshaller.unmarshal(file);
        System.out.println(socket.getAction());
        return socket.getAction();
    }

    /** Ummarshaling the ask file from client and give the information about client
     @param file is the ask file from client
     @return client information */
    public static Client getClient(File file) throws Exception
    {
        JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
        Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        XMLParse.Socket socket = (XMLParse.Socket) jaxbUnmarshaller.unmarshal(file);

        ServerClient client = socket.getServerClient();
        ArrayTaskList tasks = new ArrayTaskList();

        Task clientTask;

        for (XMLParse.TaskClient task : socket.getTasks()) {
            if (task.getInterval() == 0)
                clientTask = new Task(task.getTitle(),task.getTime(),task.getDescription());
            else
                clientTask = new Task(task.getTitle(),task.getStart(),task.getEnd(),task.getInterval(),task.getDescription());

            clientTask.setActive(task.isActive());
            tasks.add(clientTask);
        }
        return new Client(client.getLogin(), client.getPassword(), client.getId(), tasks);
    }

    /** Ummarshaling the ask file from client and give the task list for adding
     @param file is the ask file from client
     @return tasks is the task list for adding */
    public static ArrayTaskList getAddTask(File file) throws Exception
    {
        JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
        Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        XMLParse.Socket socket = (XMLParse.Socket) jaxbUnmarshaller.unmarshal(file);


        ArrayTaskList tasks = new ArrayTaskList();
        Task clientTask;

        for (XMLParse.TaskClient task : socket.getTasks()) {
            if (task.getInterval() == 0)
                clientTask = new Task(task.getTitle(),task.getTime(),task.getDescription());
            else
                clientTask = new Task(task.getTitle(),task.getStart(),task.getEnd(),task.getInterval(),task.getDescription());

            clientTask.setActive(task.isActive());
            tasks.add(clientTask);
        }

        return tasks;
    }

    /** Ummarshaling the ask file from client and give the task for delete
     @param file is the ask file from client
     @return task is the task for delete */
    public static Task getDeleteTask(File file) throws Exception
    {
        JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
        Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        XMLParse.Socket socket = (XMLParse.Socket) jaxbUnmarshaller.unmarshal(file);

        ArrayTaskList tasks = new ArrayTaskList();
        Task clientTask;

        for (XMLParse.TaskClient task : socket.getTasks()) {
            if (task.getInterval() == 0)
                clientTask = new Task(task.getTitle(),task.getTime(),task.getDescription());
            else
                clientTask = new Task(task.getTitle(),task.getStart(),task.getEnd(),task.getInterval(),task.getDescription());

            clientTask.setActive(task.isActive());
            tasks.add(clientTask);
        }

        return tasks.getTask(0);
    }

    /** Marshaling the answer file for "view" action from client
     @param client is the information about the client
     @return file is the answer file for "view" action from client */
    public static File sendTasks(Client client) throws Exception
    {
        String filename = "answerView" + client.getLogin() + ".xml";
        File file = new File(filename);
        XMLParse.ServerClient serverClient = new XMLParse.ServerClient(client.getLogin(),Integer.toString(client.getPassword()),client.getId());
        XMLParse.Socket socket = new XMLParse.Socket(serverClient, "view", 200, "Ok");
        for (Task task : client.getArrayList()) {
            socket.addTask(new XMLParse.TaskClient(task.getTitle(),task.getTime(),task.getStartTime(),task.getEndTime(),task.getRepeatInterval(),task.isActive(),task.getDescription()));
        }

        JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
        Marshaller jaxbMarshaller = jc.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(socket,file);

        return file;
    }

    /** Marshaling the answer file for "autorization" action from client
     @param client is the information about the client
     @return file is the answer file for "autorization" action from client */
    public static File sendId(Client client) throws Exception
    {
        String filename = "answerId" + client.getLogin() + ".xml";
        File file = new File(filename);
        XMLParse.ServerClient serverClient = new XMLParse.ServerClient(client.getLogin(),Integer.toString(client.getPassword()),client.getId());
        XMLParse.Socket socket = new XMLParse.Socket(serverClient, "user", 200, "Ok");

        JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
        Marshaller jaxbMarshaller = jc.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(socket,file);

        return file;
    }

    /** Marshaling the answer file for status after some action from client
     @param client is the information about the client
     @param b is "true" if ok
     @return file is the answer file for status after some action from client */
    public static File sendStatus(Client client, int code, String status) throws Exception
    {
        String filename = "answerStatus" + client.getLogin() + ".xml";
        File file = new File(filename);
        XMLParse.ServerClient serverClient = new XMLParse.ServerClient(client.getLogin(),Integer.toString(client.getPassword()),client.getId());
        
        XMLParse.Socket socket = new XMLParse.Socket(serverClient, "", code, status);

        JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
        Marshaller jaxbMarshaller = jc.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(socket,file);

        return file;
    }

    /** Marshaling the answer file for "notification" action from client
     @param client is the information about the client
     @return file is the answer file for "notification" action from client */
    public static File sendTasksByTime(Client client) throws Exception
    {
        String filename = "answerNotification" + client.getLogin() + ".xml";
        File file = new File(filename);
        XMLParse.ServerClient serverClient = new XMLParse.ServerClient(client.getLogin(),Integer.toString(client.getPassword()),client.getId());
        XMLParse.Socket socket = new XMLParse.Socket(serverClient, "notification", 200, "Ok");
        final long day = 86400000;

        Date realTime = new Date(System.currentTimeMillis());
        Date realTimeNextDay = new Date(System.currentTimeMillis() + day);
        SortedMap<Date, Set<Task>> sortedMap =
                Tasks.calendar(client.getArrayList(), realTime, realTimeNextDay);
        for(SortedMap.Entry<Date, Set<Task>> entry : sortedMap.entrySet()) {
            Date key = entry.getKey();
            Set<Task> value = entry.getValue();

            for (Task task : value) {
                if (task.isActive())
                    socket.addTask(new XMLParse.TaskClient(task.getTitle(),key.getTime(),key.getTime(),key.getTime(),0,task.isActive(),task.getDescription()));
            }
        }

        JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
        Marshaller jaxbMarshaller = jc.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(socket,file);

        return file;
    }
}




