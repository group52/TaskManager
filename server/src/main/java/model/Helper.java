package model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Date;

public class Helper {
    File storage = new File("storage.xml");
    private File directory = new File("Clientstorage");

    public ArrayTaskList getTasksByUser(Client name){
      return   name.getArrayList();
    }

    public boolean addTaskByUser(Client name, Task task){
        if (name == null){
            return false;
        }
        if (task == null){
            return false;
        }
        ArrayTaskList tempUserTaskList = name.getArrayList();
        tempUserTaskList.add(task);
        name.setArrayList(tempUserTaskList);
        addClientToStorage(name);
        return true;
    }

    public boolean deleteTaskByUser(Client name,Task task){
        if (name == null){
            return false;
        }
        if (task == null){
            return false;
        }
        ArrayTaskList tempUserTaskList = name.getArrayList();
        tempUserTaskList.remove(task);
        name.setArrayList(tempUserTaskList);
        addClientToStorage(name);
        return true;
    }

    public boolean editTaskByUser(Client name,Task taskNew,int numberTask){
        ArrayTaskList tempUserTaskList = name.getArrayList();
        if (tempUserTaskList.size()< 0 && tempUserTaskList.size() > numberTask) {
            return false;
        }

        Task taskOld = tempUserTaskList.getTask(numberTask);
        if (taskOld.equals(taskNew)){
            return false;
        }
        if (taskOld.isRepeated()&& taskNew.isRepeated()){
            updateTask(taskOld,taskNew,2);
            return true;
        }
        else if (taskOld.isRepeated()&& !taskNew.isRepeated()){
            updateTask(taskOld,taskNew,2);
            return true;
        }
        else if (!taskOld.isRepeated()&& taskNew.isRepeated()){
            updateTask(taskOld,taskNew,1);
            return true;
        }
        else {
            updateTask(taskOld,taskNew,1);
            return true;
        }
    }

    private void updateTask(Task taskOld,Task taskNew,int variant){
        if (variant == 1){
            taskOld.setTitle(taskNew.getTitle());
            Date time = new Date(taskNew.getTime());
            taskOld.setTime(time);
            taskOld.setActive(taskNew.isActive());
        }
        else {
            taskOld.setTitle(taskNew.getTitle());
            Date start = new Date(taskNew.getStartTime());
            Date end = new Date(taskNew.getEndTime());
            taskOld.setTime(start, end, taskNew.getRepeatInterval());
            taskOld.setActive(taskNew.isActive());
        }
    }

    public File findUser(String name){
        //make directory
        if (!directory.exists()){
            directory.mkdir();
        }
        File allfiles [] = directory.listFiles();
        for (File tempFile:allfiles){
            String newName =  stripExtension(tempFile.getName());
          if ( newName.equals(name)){
              return tempFile;
          }
        }

        return null;
    }

    public void addClientToStorage(Client client){
        File userFile = new File("Clientstorage" + "\\" + client.getLogin() + ".xml");
        marshalUsers(client,userFile);
    }

    private void marshalUsers(Client client,File userFile ){
        try {
            JAXBContext parser = JAXBContext.newInstance(Client.class);
            Marshaller marshaller = parser.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
            marshaller.marshal(client,userFile);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    public Client getClientFromStorage(String name){
        try {
            File userFile =  this.findUser(name);
            if (userFile != null) {
                JAXBContext parser = JAXBContext.newInstance(Client.class);
                Unmarshaller unmarshaller = parser.createUnmarshaller();
                Client tempstorage = (Client) unmarshaller.unmarshal(userFile);
                return tempstorage;
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean autorisation (Client client){
        if (client==null){
            return false;
        }

        Client tempUser = this.getClientFromStorage(client.getLogin());
        if (tempUser == null){
            return false;
        }
         else if (client.getPassword().equals(tempUser.getPassword())){
            return true;
        }
        else
            return false;
    }

    public boolean isClientExist(Client client){

        if (client == null){
            return false;
        }
        if (!directory.exists()){
            directory.mkdir();
        }
        File allfiles [] = directory.listFiles();
        if (allfiles == null){
            return false;
        }
        for (int i = 0; i < allfiles.length; i++){
       String newName =  stripExtension(allfiles[i].getName());
            if ( newName.equals(client.getLogin())){
                return true;
            }
        }
        return false;
    }

   private  String stripExtension (String str) {
        if (str == null) return null;
        int pos = str.lastIndexOf(".");
        if (pos == -1) return str;
        return str.substring(0, pos);
    }
}

