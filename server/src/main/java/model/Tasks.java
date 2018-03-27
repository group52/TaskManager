package model;

import java.util.Date;
import java.util.SortedMap; 
import java.util.TreeMap; 
import java.util.Set; 
import java.util.HashSet;
import org.apache.log4j.Logger;

/** class Tasks for work with collections <Task> */
public class Tasks { 

    private static final Logger log = Logger.getLogger(Tasks.class);

    /** Return the list of tasks between the time period 
    @param tasks is list of Iterable<Task>
    @param from is the start of the period
    @param to is the end of the period
    @return the list of tasks between the time period */ 
    public static Iterable<Task> incoming(Iterable<Task> tasks, 
    Date from, Date to) {
        Date dateLocal = new Date();  
        Date dateLocal2 = new Date();  
        dateLocal2.setTime((long) -1);
        
        Set<Task> tasksNew = new HashSet<Task>();
        
        for (Task task : tasks) {            
            try {
                dateLocal = task.nextTimeAfter(from);
                if (dateLocal != null)
                    if ((dateLocal.before(to) || dateLocal.equals(to))
                        && dateLocal.after(from)) {
                        tasksNew.add(task);                
                    }  
            } catch (NullPointerException e) {
               log.error("Tasks.incoming() failed", e);
            }
        }

        return tasksNew;
    }
    
    /** Return the sorted map by date of tasks between the time period 
    @param tasks is list of Iterable<Task>
    @param from is the start of the period
    @param to is the end of the period
    @return the sorted map by date between the time period */
    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks, 
    Date from, Date to) {
    
        Date dateLocal = new Date();          
  
        SortedMap<Date, Set<Task>> sortedMap = new TreeMap<Date, Set<Task>>();
        Set<Task> setNew = new HashSet<Task>();
        
        for (Task task : tasks) { 
            
            dateLocal = task.nextTimeAfter(from);
            if (dateLocal != null)
                while (dateLocal != null &&(dateLocal.before(to) || dateLocal.equals(to))
                    && dateLocal.after(from)) {
                    if (sortedMap.get(dateLocal) != null) {
                        setNew = sortedMap.get(dateLocal);
                        setNew.add(task);
                    } else {
                        setNew = new HashSet<Task>();                
                        setNew.add(task);
                        sortedMap.put(dateLocal, setNew);
                    }
                    
                    dateLocal = task.nextTimeAfter(dateLocal);
                }                      
        }

        return sortedMap;
    } 
}