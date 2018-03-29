package model;

import org.apache.log4j.Logger;
/** class Client give information about user for the controller */
public class Client {

    private Logger log = Logger.getLogger(Client.class);

    private String login;
    private String password;
    private ArrayTaskList arrayList;
    private int id = 0;

    /** Empty constructor for the client */
    public Client() {
        this.login = "";
        this.password ="";
        this.arrayList = new ArrayTaskList();
    }

    /** Return the login of the client
    @return login is the login of the client */
    public String getLogin() {
        return login;
    }
    
    /** Return the password of the client
    @return password is the password of the client */
    public String getPassword() {
        return password;
    }

    /** Return the tasks list of the client
    @return arrayList is the tasks list of the client */
    public ArrayTaskList getArrayList() {
        return arrayList;
    }

    /** Return the session id of the client, which is new for each autorization
    @return id is the session id of the client */
    public int getId() {
        return id;
    }

    /** Setup the task list for the client
    @param arrayList is the task list for the client */
    public void setArrayList(ArrayTaskList arrayList) {
        this.arrayList = arrayList;
    }

    /** Setup the session id for the client
    @param id is the session id for the client */
    public void setId(int id) {
        this.id = id;
    }
    
    /** Setup the password for the client
    @param password is the password for the client */
    public void setPassword(String password) {
        this.password = password;
    }

    /** Constructor for the client
    @param login is the login of the client
    @param password is the password of the client
    @param arrayList is the task list for the client */
    public Client(String login, String password, ArrayTaskList arrayList){
        this.login = login;
        this.password = password;
        this.arrayList = arrayList;
    }

    /** Constructor for the client
    @param login is the login of the client
    @param password is the password of the client
    @param id is the session id of the client 
    @param arrayList is the task list for the client */
    public Client(String login, String password, int id, ArrayTaskList arrayList){
        this.login = login;
        this.password = password;
        this.id = id;
        this.arrayList = arrayList;
    }

    /** Add tasks to the task list of the client
    @param arrayList is the task list for adding to the client */
    public void addArrayList(ArrayTaskList arrayList) {
        for (Task task : arrayList) {
            this.arrayList.add(task);
        }
    }

    /** Delete task from the task list of the client
    @param task is the task for deleting from the client task list*/
    public boolean deleteArrayList(Task task) {
        return this.arrayList.remove(task);
    }

    /** Indicates whether some other object is "equal to" this one
     @return true if this object is the same as the obj argument;
     false otherwise.
     @param obj - the reference object with which to compare */
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (obj == null) return false;

        if (getClass() != obj.getClass()) return false;

        Client other = (Client) obj;

        return login.equals(other.login)
                && password == other.password
                && arrayList.equals(other.arrayList);
    }

    /** Returns a hash code value for the object
     @return a hash code value for this object */
    public int hashCode() {
        final int primeNumber1 = 7;
        final int primeNumber2 = 11;
        final int primeNumber3 = 13;


        return primeNumber1 * login.hashCode()
                + primeNumber2 * new Integer(password).hashCode()
                + primeNumber3 * arrayList.hashCode();
    }

    /** Returns a string representation of the object
     @return a string representation of the object */
    public String toString() {
        return getClass().getName()
                + "[login = " + login
                + ", ArrayTaskList = " + arrayList.toString()
                + "]";
    }

    /** Creates and returns a copy of this object
     @return a clone of this instance
     @throws CloneNotSupportedException - if the object's class does not
     support the Cloneable interface */
    public Client clone() throws CloneNotSupportedException {

        Client cloned = (Client) super.clone();

        return cloned;
    }
}
