
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;

public class XMLParse {

    @XmlRootElement (name = "server")
    class Server {
        private ArrayList<XMLParse.ServerClient> clients; // внутреннее представление списка
        public Server(){
            clients = new ArrayList<XMLParse.ServerClient>();
        }

        public void add(XMLParse.ServerClient client){
            clients.add(client);
        }

        @XmlElement(name = "client")
        public List<XMLParse.ServerClient> getServer() {
            return clients;
        }
    }

    @XmlRootElement(name = "client")
    static class ServerClient {
        // поля
        @XmlAttribute(name = "login")
        String login;
        @XmlAttribute(name = "password")
        int password;
        @XmlElement
        int id;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public int getPassword() {
            return password;
        }

        public void setPassword(int password) {
            this.password = password;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public ServerClient() {
        }

        public ServerClient(String login, int password, int id) {
            setLogin(login);
            setPassword(password);
            setId(id);
        }

    }

    @XmlRootElement (name = "socket")
    static class Socket {
        private ArrayList<XMLParse.TaskClient> tasks;

        @XmlElement
        XMLParse.ServerClient serverClient;

        @XmlElement
        String action;

        @XmlElement
        int code;

        @XmlElement
        String status;

        @XmlElement(name = "task")
        public List<XMLParse.TaskClient> getTasks() {
            return tasks;
        }

        public void setTasks(ArrayList<XMLParse.TaskClient> tasks) {
            this.tasks = tasks;
        }

        public ServerClient getServerClient() {
            return serverClient;
        }

        public void setServerClient(ServerClient serverClient) {
            this.serverClient = serverClient;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Socket(){
            serverClient = new ServerClient();
            action = "";
            code = 0;
            status = "";
            tasks = new ArrayList<XMLParse.TaskClient>();
        }

        public Socket(XMLParse.ServerClient serverClient, String action, int code, String status){
            setServerClient(serverClient);
            setAction(action);
            setCode(code);
            setStatus(status);
            tasks = new ArrayList<XMLParse.TaskClient>();
        }

        public void addTask(XMLParse.TaskClient task){
            tasks.add(task);
        }
    }

    @XmlRootElement (name = "task")
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public long getStart() {
            return start;
        }

        public void setStart(long start) {
            this.start = start;
        }

        public long getEnd() {
            return end;
        }

        public void setEnd(long end) {
            this.end = end;
        }

        public int getInterval() {
            return interval;
        }

        public void setInterval(int interval) {
            this.interval = interval;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public TaskClient() {
        }

        public TaskClient(String title, long time, long start, long end, int interval, boolean active) {
            setTitle(title);
            setTime(time);
            setStart(start);
            setEnd(end);
            setInterval(interval);
            setActive(active);
        }
    }

    public static void addClient(Client client) throws Exception {

        String file = "server.xml";

        JAXBContext jc = JAXBContext.newInstance(XMLParse.Server.class);
        Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        XMLParse.Server server = (XMLParse.Server) jaxbUnmarshaller.unmarshal(new File(file));

        server.add(new ServerClient(client.getLogin(),client.getPassword(),client.getId()));

        Marshaller jaxbMarshaller = jc.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(server,new File(file));
    }

    public static void addTask(Client client, Task task) throws Exception {

        String file = "" + client.getLogin() + ".xml";

        JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
        Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        XMLParse.Socket socket = (XMLParse.Socket) jaxbUnmarshaller.unmarshal(new File(file));

        socket.addTask(new XMLParse.TaskClient(task.getTitle(),task.getTime(),task.getStartTime(),task.getEndTime(),task.getRepeatInterval(),task.isActive()));

        Marshaller jaxbMarshaller = jc.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(socket,new File(file));
    }

    public static String getCommandType(File file) throws Exception
    {
        JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
        Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        XMLParse.Socket socket = (XMLParse.Socket) jaxbUnmarshaller.unmarshal(file);

        return socket.getAction();
    }


    public static Client getClient(File file) throws Exception
    {
        JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
        Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        XMLParse.Socket socket = (XMLParse.Socket) jaxbUnmarshaller.unmarshal(file);

        ServerClient client = socket.getServerClient();
        ArrayTaskList tasks = new ArrayTaskList();

        for (XMLParse.TaskClient task : socket.getTasks()) {
            if (task.getInterval() == 0) {
                Task clientTask = new Task(task.getTitle(),task.getTime());
                clientTask.setActive(task.isActive());
                tasks.add(clientTask);
            }
        }

        return new Client(client.getLogin(), client.getPassword(), client.getId(), tasks);
    }

    public static ArrayTaskList getAddTask(File file) throws Exception
    {
        JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
        Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        XMLParse.Socket socket = (XMLParse.Socket) jaxbUnmarshaller.unmarshal(file);


        ArrayTaskList tasks = new ArrayTaskList();

        for (XMLParse.TaskClient task : socket.getTasks()) {
            if (task.getInterval() == 0) {
                Task clientTask = new Task(task.getTitle(),task.getTime());
                clientTask.setActive(task.isActive());
                tasks.add(clientTask);
            }
        }

        return tasks;
    }

    public static Task getDeleteTask(File file) throws Exception
    {
        JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
        Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        XMLParse.Socket socket = (XMLParse.Socket) jaxbUnmarshaller.unmarshal(file);


        ArrayTaskList tasks = new ArrayTaskList();
        Task clientTask;

        for (XMLParse.TaskClient task : socket.getTasks()) {
            if (task.getInterval() == 0)
                clientTask = new Task(task.getTitle(),task.getTime());
            else
                clientTask = new Task(task.getTitle(),task.getStart(),task.getEnd(),task.getInterval());

            clientTask.setActive(task.isActive());
            tasks.add(clientTask);
        }

        return tasks.getTask(0);

    }

    public static File sendTasks(Client client) throws Exception
    {
        String filename = "answer" + client.getLogin() + ".xml";
        File file = new File(filename);
        XMLParse.ServerClient serverClient = new XMLParse.ServerClient(client.getLogin(),client.getPassword(),client.getId());
        XMLParse.Socket socket = new XMLParse.Socket(serverClient, "view", 200, "Ok");
        for (Task task : client.getArrayList()) {
            socket.addTask(new XMLParse.TaskClient(task.getTitle(),task.getTime(),task.getStartTime(),task.getEndTime(),task.getRepeatInterval(),task.isActive()));
        }

        JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
        Marshaller jaxbMarshaller = jc.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(socket,file);

        return file;
    }

    public static File sendId(Client client) throws Exception
    {
        String filename = "answer" + client.getLogin() + ".xml";
        File file = new File(filename);
        XMLParse.ServerClient serverClient = new XMLParse.ServerClient(client.getLogin(),client.getPassword(),client.getId());
        XMLParse.Socket socket = new XMLParse.Socket(serverClient, "avtorization", 200, "Ok");

        JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
        Marshaller jaxbMarshaller = jc.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(socket,file);

        return file;

    }

    public static File sendStatus(Client client, boolean b) throws Exception
    {
        String filename = "answer" + client.getLogin() + ".xml";
        File file = new File(filename);
        XMLParse.ServerClient serverClient = new XMLParse.ServerClient(client.getLogin(),client.getPassword(),client.getId());
        String status;
        if (b)
            status = "Ok";
        else
            status = "False";


        XMLParse.Socket socket = new XMLParse.Socket(serverClient, "", 200, status);

        JAXBContext jc = JAXBContext.newInstance(XMLParse.Socket.class);
        Marshaller jaxbMarshaller = jc.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(socket,file);

        return file;
    }
}




