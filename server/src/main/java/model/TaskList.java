package model;

import java.util.Iterator;
import java.io.Serializable;


/** the abstract list of task */
public abstract class TaskList 
    implements Iterable<Task>, Cloneable, Serializable {

    /** Returns an iterator over elements of type Task 
    @return an iterator over a set of elements of type Task */
    public abstract Iterator<Task> iterator();    
    
    
    /** Adding new task to the TaskList
    @param task is new task for adding */
    public abstract void add(Task task); 

    /** Removing new task from the TaskList
    @param task is the task for removing
    @return true if find the task for removing */
    public abstract boolean remove(Task task);
    

    /** Return size of the TaskList
    @return size of the TaskList */
    public abstract int size();

    /** Return the task with index
    @param index give the task index in the TaskList
    @return give the task with index */
    public abstract Task getTask(int index);
      
}