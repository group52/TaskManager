package model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
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
import org.apache.log4j.Logger;

/** class XMLParse for parsing, marshaling and unmarshaling the *.xml files from the client to the controller */
public class XMLParse {

    private Logger log = Logger.getLogger(XMLParse.class);

    public XMLParse() {
    }

    /**
     * subclass Server for saving information in xml-format on the controller side
     */
    @XmlRootElement(name = "controller")
    @XmlAccessorType(XmlAccessType.FIELD)
 static  class Server {

        @XmlElement(name = "client")
        private ArrayList<XMLParse.ServerClient> clients;

        /**
         * Empty constructor without clients on the controller
         */
        public Server() {
            clients = new ArrayList<>();
        }

        /**
         * Add client to the controller
         *
         * @param client is the client for adding
         */
        public void add(XMLParse.ServerClient client) {
            clients.add(client);
        }

        /**
         * Add client to the controller
         *
         * @return clients is the list of the clients
         */
        public List<XMLParse.ServerClient> getClients() {
            return clients;
        }
    }

    /**
     * Subclass ServerClien give information about user for the controller
     */
    @XmlRootElement(name = "client")
    @XmlAccessorType(XmlAccessType.FIELD)
   static class ServerClient {

        @XmlAttribute(name = "login")
        String login;
        @XmlAttribute(name = "password")
        String password;
        @XmlElement(name = "session_id")
        int id;

        /**
         * Return the login of the client
         *
         * @return login is the login of the client
         */
        public String getLogin() {
            return login;
        }

        /**
         * Setup the login for the client
         *
         * @param login is the password for the client
         */
        public void setLogin(String login) {
            this.login = login;
        }

        /**
         * Return the password of the client
         *
         * @return password is the password of the client
         */
        public String getPassword() {
            return password;
        }

        /**
         * Setup the password for the client
         *
         * @param password is the password for the client
         */
        public void setPassword(String password) {
            this.password = password;
        }

        /**
         * Return the session id of the client, which is new for each autorization
         *
         * @return id is the session id of the client
         */
        public int getId() {
            return id;
        }

        /**
         * Setup the session id for the client
         *
         * @param id is the session id for the client
         */
        public void setId(int id) {
            this.id = id;
        }

        /**
         * Empty constructor for the client
         */
        public ServerClient() {
        }

        /**
         * Constructor for the client
         *
         * @param login    is the login of the client
         * @param password is the password of the client
         * @param id       is the session id of the client
         */
        public ServerClient(String login, String password, int id) {
            setLogin(login);
            setPassword(password);
            setId(id);
        }

    }

    /**
     * subclass Socket for sending and receiving information in xml-format between the controller and client
     */
    @XmlRootElement (name = "socket")
    @XmlAccessorType(XmlAccessType.FIELD)
   static class Socket {

        @XmlElement (name = "client")
        private ServerClient client;

        @XmlElement (name = "action")
        private String action;

        @XmlElement (name = "code")
        private int code;

        @XmlElement (name = "status")
        private String status;

        @XmlElement(name = "task")
        private ArrayList<XMLParse.TaskClient> tasks;



        /**
         * Return the tasks of the client
         *
         * @return tasks is the tasks of the client
         */
        public List<XMLParse.TaskClient> getTasks() {
            return tasks;
        }

        /**
         * Setup the tasks to the task list
         *
         * @param tasks is the tasks of the task list
         */
        public void setTasks(ArrayList<XMLParse.TaskClient> tasks) {
            this.tasks = tasks;
        }

        /**
         * Return the client information
         *
         * @return serverClient is the client information
         */
        public ServerClient getServerClient() {
            return client;
        }

        /**
         * Setup the client information
         *
         * @param serverClient is the client information
         */
        public void setServerClient(ServerClient serverClient) {
            this.client = serverClient;
        }

        /**
         * Return the action which the client want to do with the tasks list
         *
         * @return action is the action which the client want to do
         */
        public String getAction() {
            return action;
        }

        /**
         * Setup the action of the controller work
         *
         * @param action is the action of the controller work
         */
        public void setAction(String action) {
            this.action = action;
        }

        /**
         * Return the code of the problem with client information from controller side
         *
         * @return code is the problem with client information
         */
        public int getCode() {
            return code;
        }

        /**
         * Setup the code of the problem with client information from controller side
         *
         * @param code is the code of the problem with client information
         */
        public void setCode(int code) {
            this.code = code;
        }

        /**
         * Return the status of controller answer ("true" or "false")
         *
         * @return status is the status of controller answer
         */
        public String getStatus() {
            return status;
        }

        /**
         * Setup the status of controller answer ("true" or "false")
         *
         * @param status is the status of controller answer
         */
        public void setStatus(String status) {
            this.status = status;
        }

        /**
         * Empty constructor for the socket
         */
        public Socket() {
            client = new ServerClient();
            action = "";
            code = 0;
            status = "";
            tasks = new ArrayList<>();
        }

        /**
         * Constructor for the one .xml file for send
         *
         * @param serverClient is the client information
         * @param action       is the action of the controller work
         * @param code         is the code of the problem with client information
         * @param status       is the status of controller answer
         */
        public Socket(XMLParse.ServerClient serverClient, String action, int code, String status) {
            setServerClient(serverClient);
            setAction(action);
            setCode(code);
            setStatus(status);
            tasks = new ArrayList<>();
        }

        /**
         * Add task to the client task list
         *
         * @param task is the new task for addding
         */
        public void addTask(XMLParse.TaskClient task) {
            tasks.add(task);
        }
    }

    /**
     * subclass TaskClient is the task of the client
     */
    @XmlRootElement(name = "task")
    @XmlAccessorType(XmlAccessType.FIELD)
  static  class TaskClient {
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

        /**
         * Return the description of the task
         *
         * @return the description of the task
         */
        public String getDescription() {
            return description;
        }

        /**
         * Setup the description of the task
         *
         * @param description is the description of the task
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * Return the title of the task
         *
         * @return the title of the task
         */
        public String getTitle() {
            return title;
        }

        /**
         * Setup the title of the task
         *
         * @param title is the title of the task
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * Return the start time of the task
         *
         * @return the start time of the repeated task
         * or time of the non repeated task
         */
        public long getTime() {
            return time;
        }

        /**
         * Setup the time of the non repeated task
         * and make non repeated task for repeated task
         *
         * @param time is the time of the non repeated task
         */
        public void setTime(long time) {
            this.time = time;
        }

        /**
         * Return the same as method getTime()
         *
         * @return the start time of the repeated task
         * or time of the non repeated task
         */
        public long getStart() {
            return start;
        }

        /**
         * Setup the start of the task
         *
         * @param start is the start time of the task
         */
        public void setStart(long start) {
            this.start = start;
        }

        /**
         * Return the end time of the repeated task
         * or time of the non repeated task
         *
         * @return the end time of the repeated task
         * or time of the non repeated task
         */
        public long getEnd() {
            return end;
        }

        /**
         * Setup the end of the repeated task
         *
         * @param end is the end of repeated period
         */
        public void setEnd(long end) {
            this.end = end;
        }

        /**
         * Return the time between two same repeated tasks
         * or 0 for the non repeated task
         *
         * @return the time between two same repeated tasks
         * or 0 for the non repeated task
         */
        public int getInterval() {
            return interval;
        }

        /**
         * Setup the interval of the repeated task
         *
         * @param interval is the time between two same tasks
         */
        public void setInterval(int interval) {
            this.interval = interval;
        }

        /**
         * Return the activity of the task
         *
         * @return the activity of the task
         */
        public boolean isActive() {
            return active;
        }

        /**
         * Setup the activity of the task
         *
         * @param active is the activity of the task
         */
        public void setActive(boolean active) {
            this.active = active;
        }

        /**
         * Empty constructor for the task
         */
        public TaskClient() {
        }

        /**
         * Constructor for the tasks
         *
         * @param title       is the title of the task;
         * @param start       is the start time of the task
         * @param end         is the end of repeated period
         * @param interval    is the time between two same tasks
         * @param description is the description of the task
         */
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

    /**
     * Add the new client to "controller.xml" on controller side
     *
     * @param client is the new client for adding
     */
    public void addClient(Client client) {
        try {
            File file = new File(new java.io.File("xml/controller.xml").getAbsolutePath());
            XMLParse.Server server;
            JAXBContext jc = JAXBContext.newInstance(XMLParse.Server.class);

            if (file.isFile()) {
                Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
                server = (XMLParse.Server) jaxbUnmarshaller.unmarshal(file);
            }
            else {
                server = new XMLParse.Server();
            }

            ServerClient serverClient = new ServerClient(client.getLogin(), client.getPassword(), client.getId());
            server.add(serverClient);

            Marshaller jaxbMarshaller = jc.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(server, file);

        } catch (JAXBException e) {
            log.error("JAXBException: ", e);
        }
    }

    /**
     * Change session id of the client in "controller.xml" on controller side
     *
     * @param client is the client information
     */
    public boolean newSessionClient(Client client) {
        try {
            File file = new File(new java.io.File("xml/controller.xml").getAbsolutePath());
            XMLParse.Server server;
            JAXBContext jc = JAXBContext.newInstance(XMLParse.Server.class);

            if (file.isFile()) {
                Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
                server = (XMLParse.Server) jaxbUnmarshaller.unmarshal(file);
            }
            else {
                server = new XMLParse.Server();
            }

            List<XMLParse.ServerClient> clientsList = server.getClients();

            for (XMLParse.ServerClient serverClient : clientsList) {
                if (serverClient.getLogin().equals(client.getLogin())
                        && (serverClient.getPassword()).equals(client.getPassword())) {
                    serverClient.setId(client.getId());

                    Marshaller jaxbMarshaller = jc.createMarshaller();
                    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    jaxbMarshaller.marshal(server, file);
                    return true;
                }
            }
        } catch (JAXBException e) {
            log.error("JAXBException: ", e);
        }
        return false;
    }

    /**
     * Find only login in "controller.xml" on controller side
     *
     * @param client is the client information
     */
    public boolean findLogin(Client client) {
        try {
            File file = new File(new java.io.File("xml/controller.xml").getAbsolutePath());

            if (file.isFile()) {
                JAXBContext jc = JAXBContext.newInstance(XMLParse.Server.class);
                Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
                XMLParse.Server server = (XMLParse.Server) jaxbUnmarshaller.unmarshal(file);

                List<XMLParse.ServerClient> clientsList = server.getClients();

                for (XMLParse.ServerClient serverClient : clientsList) {
                    if (serverClient.getLogin().equals(client.getLogin())) {
                        return true;
                    }
                }
            }
            else
                return false;

        } catch (JAXBException e) {
            log.error("JAXBException: ", e);
        }

        return false;
    }

    /**
     * Chack all information about the client in "controller.xml" on controller side
     *
     * @param client is the client information
     */
    public boolean findClient(Client client) {
        try {
            File file = new File(new java.io.File("xml/controller.xml").getAbsolutePath());

            if (file.isFile()) {
                JAXBContext jc = JAXBContext.newInstance(XMLParse.Server.class);
                Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
                XMLParse.Server server = (XMLParse.Server) jaxbUnmarshaller.unmarshal(file);

                List<XMLParse.ServerClient> clientsList = server.getClients();

                for (XMLParse.ServerClient serverClient : clientsList) {
                    if (serverClient.getLogin().equals(client.getLogin())
                            && serverClient.getPassword().equals(client.getPassword())
                            && serverClient.getId() == client.getId()) {
                        return true;
                    }
                }
            }
            else
                return false;

        } catch (JAXBException e) {
            log.error("JAXBException: ", e);
        }

        return false;
}

    /**  Change information about the client in "login.xml" on controller side
     @param client is the client information */
    public void changeClient(Client client) {
        try {
            File file =  new File(new java.io.File("xml/" + client.getLogin() + ".xml").getAbsolutePath());
            XMLParse.ServerClient serverClient = new XMLParse.ServerClient(client.getLogin(),client.getPassword(),client.getId());
            XMLParse.Socket socket = new XMLParse.Socket(serverClient, "", 200, "Ok");
            if(client.getArrayList().size() != 0)
                for (Task task : client.getArrayList()) {
                    socket.addTask(new XMLParse.TaskClient(task.getTitle(),task.getTime(),task.getStartTime(),task.getEndTime(),task.getRepeatInterval(),task.isActive(),task.getDescription()));
                }

            JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
            Marshaller jaxbMarshaller = jc.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(socket,file);

        } catch (JAXBException e) {
            log.error("JAXBException: ", e);
        }
    }


    /** Ummarshaling the ask from client and give the class Socket
     @param s is the ask from client
     @return socket information */
    public Socket inParse(String s) {
        log.debug("inParse: " + s);
        Socket socket;

        try {
            JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
            Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
            socket = (XMLParse.Socket) jaxbUnmarshaller.unmarshal(new StringReader(s));
            return socket;
        } catch (JAXBException e) {
            log.error("JAXBException: ", e);
            socket = new Socket();
        }
        return socket;
    }

    /** Ummarshaling the ask file from client and give the information about client
     @param s is the ask file from client
     @return client information */
    public Client getClient(String s) {
        Socket socket = inParse(s);

        ServerClient client = socket.getServerClient();
        ArrayTaskList tasks = new ArrayTaskList();

        Task clientTask;

        if (socket.getTasks().size() > 0)
            for (TaskClient task : socket.getTasks()) {
                if (task.getInterval() == 0)
                    clientTask = new Task(task.getTitle(),task.getTime(),task.getDescription());
                else
                    clientTask = new Task(task.getTitle(),task.getStart(),task.getEnd(),task.getInterval(),task.getDescription());

                clientTask.setActive(task.isActive());
                tasks.add(clientTask);
            }

        return new Client(client.getLogin(), client.getPassword(), client.getId(), tasks);
    }

    /** Ummarshaling the ask from client and give the class Socket
     @param file is the file client
     @return socket information */
    public Socket inParse(File file) {

        log.debug("inParse File" + file.getName());

        Socket socket;

        try {
            JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
            Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
            socket = (XMLParse.Socket) jaxbUnmarshaller.unmarshal(file);
            return socket;

        } catch (JAXBException e) {
            log.error("JAXBException: ", e);
            socket = new Socket();
        }

        return socket;
    }

    /** Ummarshaling the ask file  and give the information about client
     @param file is the ask file from client
     @return client information */
    public Client getClient(File file) {

        Socket socket = inParse(file);
        ArrayTaskList tasks = new ArrayTaskList();
        ServerClient serverClient = socket.getServerClient();
        Task clientTask;

        if (socket.getTasks().size() > 0)
            for (XMLParse.TaskClient task : socket.getTasks()) {
                if (task.getInterval() == 0)
                    clientTask = new Task(task.getTitle(),task.getTime(),task.getDescription());
                else
                    clientTask = new Task(task.getTitle(),task.getStart(),task.getEnd(),task.getInterval(),task.getDescription());

                clientTask.setActive(task.isActive());
                tasks.add(clientTask);
            }

        return new Client(serverClient.getLogin(), serverClient.getPassword(), serverClient.getId(), tasks);
    }

    /** Ummarshaling the ask file from client and give the task list for adding
     @param s is the ask from client
     @return tasks is the task list for adding */
    public ArrayTaskList getAddTask(String s) {

        Socket socket = inParse(s);

        ArrayTaskList tasks = new ArrayTaskList();
        Task clientTask;

        for (TaskClient task : socket.getTasks()) {
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
     @param s is the ask from client
     @return task is the task for delete */
    public Task getDeleteTask(String s) {

        Socket socket = inParse(s);

        ArrayTaskList tasks = new ArrayTaskList();
        Task clientTask;

        if (socket.getTasks().size() > 0)
            for (TaskClient task : socket.getTasks()) {
                if (task.getInterval() == 0)
                    clientTask = new Task(task.getTitle(),task.getTime(),task.getDescription());
                else
                    clientTask = new Task(task.getTitle(),task.getStart(),task.getEnd(),task.getInterval(),task.getDescription());

                clientTask.setActive(task.isActive());
                tasks.add(clientTask);
            }

        return tasks.getTask(0);
    }

    /** Marshaling the answer file for "autorization" action from client
     @param socket is the information to send to the client
     @return String is the answer for action from client */
    public String outParse(Socket socket) {

        String text;
        try {
            StringWriter sw = new StringWriter();

            JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
            Marshaller jaxbMarshaller = jc.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(socket,sw);
            text = sw.toString().replaceAll("\n","");

        } catch (JAXBException e) {
            log.error("JAXBException: ", e);
            text = "";
        }

        return text;
    }

    /** Marshaling the answer file for "view" action from client
     @param client is the information about the client
     @return file is the answer file for "view" action from client */
    public String sendTasks(Client client) {

        XMLParse.ServerClient serverClient = new XMLParse.ServerClient(client.getLogin(),client.getPassword(),client.getId());
        XMLParse.Socket socket = new XMLParse.Socket(serverClient, "view", 200, "Ok");

        if (client.getArrayList().size() > 0)
            for (Task task : client.getArrayList()) {
                socket.addTask(new XMLParse.TaskClient(task.getTitle(),task.getTime(),task.getStartTime(),task.getEndTime(),task.getRepeatInterval(),task.isActive(),task.getDescription()));
            }

        return outParse(socket);
    }

    /** Marshaling the answer file for "autorization" action from client
     @param client is the information about the client
     @return file is the answer file for "autorization" action from client */
    public String sendId(Client client) {

        ServerClient serverClient = new ServerClient(client.getLogin(),client.getPassword(),client.getId());
        Socket socket = new Socket(serverClient, "user", 200, "Ok");

        return outParse(socket);
    }

    /** Marshaling the answer file for status after some action from client
     @param client is the information about the client
     @param code is the answer code to the client
     @param status is the answer to client
     @return file is the answer file for status after some action from client */
    public String sendStatus(Client client, int code, String status) {

        XMLParse.ServerClient serverClient = new XMLParse.ServerClient(client.getLogin(),client.getPassword(),client.getId());
        XMLParse.Socket socket = new XMLParse.Socket(serverClient, "", code, status);
        return outParse(socket);
    }

    /** Marshaling the answer file for "notification" action from client
     @param client is the information about the client
     @return file is the answer file for "notification" action from client */
    public String sendTasksByTime(Client client) {

        ServerClient serverClient = new ServerClient(client.getLogin(),client.getPassword(),client.getId());
        Socket socket = new Socket(serverClient, "notification", 200, "Ok");
        final long hour = 86400000/24;
        final long day = 86400000;

        Date realTime = new Date(System.currentTimeMillis());
        Date realTimeNextHour = new Date(System.currentTimeMillis() + hour);

        int numberTask = 0;
        if (client.getArrayList().size() > 0) {
            SortedMap<Date, Set<Task>> sortedMap =
                    Tasks.calendar(client.getArrayList(), realTime, realTimeNextHour);

            if (sortedMap.size() == 0) {
                Date realTimeNextDay = new Date(System.currentTimeMillis() + day);
                sortedMap = Tasks.calendar(client.getArrayList(), realTime, realTimeNextDay);
            }

            if (sortedMap.size() != 0)
                for (SortedMap.Entry<Date, Set<Task>> entry : sortedMap.entrySet()) {
                    Date key = entry.getKey();
                    Set<Task> value = entry.getValue();

                    for (Task task : value) {
                        if (task.isActive()) {
                            socket.addTask(new TaskClient(task.getTitle(), key.getTime(), key.getTime(), key.getTime(), 0, task.isActive(), task.getDescription()));
                            numberTask += 1;
                            if (numberTask >= 10)
                                return outParse(socket);
                        }
                    }
                }
        }

        return outParse(socket);
    }
}




