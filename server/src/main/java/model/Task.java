package model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.io.Serializable;
import org.apache.log4j.Logger;

/** class Task for planning the day */
@XmlRootElement(name = "Task")
@XmlAccessorType(XmlAccessType.FIELD)
public class Task implements Cloneable, Serializable {

    private static final Logger log = Logger.getLogger(Task.class);

    @XmlElement(name = "title")
    private String title;

    @XmlElement(name = "time")
    //@XmlJavaTypeAdapter(DateTimeAdapter.class)
    private Date time;

    @XmlElement(name = "start")
    //@XmlJavaTypeAdapter(DateTimeAdapter.class)
    private Date start;

    @XmlElement(name = "end")
    //@XmlJavaTypeAdapter(DateTimeAdapter.class)
    private Date end;

    @XmlElement(name = "interval")
    private int interval;

    @XmlElement(name = "active")
    private boolean active;

    @XmlElement(name = "description")
    private String description;

    // empty constructor for JAX
    public Task(){

    }
       
    /** Constructor for non repeated tasks
    @param title is the title of the task
    @param time is the start time of the non repeated task
    @param description is the description of the task */
    public Task(String title, long time, String description) {

        this.title = title;        
        this.time = new Date(time);
        this.start = new Date(time);
        this.end = new Date(time);
        this.interval = 0;
        this.active = false;
        this.description = description;
    }


    /** Constructor for repeated tasks
    @param title is the title of the task;
    @param start is the start time of the task
    @param end is the end of repeated period
    @param interval is the time between two same tasks
    @param description is the description of the task */
    public Task(String title, long start, long end, int interval, String description) {

        this.title = title;
        this.time = new Date(start);
        this.start = new Date(start);
        this.end = new Date(end);
        this.interval = interval;
        this.active = false;
        this.description = description;
    }

    /** Constructor for non repeated tasks
    @param title is the title of the task
    @param time is the start time of the non repeated task
    @param description is the description of the task */
    public Task(String title, Date time, String description) {
        if (title != null && time != null) {
            this.title = title;
            this.time = time;
            this.start = time;
            this.end = time;
            this.interval = 0;
            this.active = false;
            this.description = description;
        }
    }


    /** Constructor for repeated tasks
    @param title is the title of the task;
    @param start is the start time of the task
    @param end is the end of repeated period
    @param interval is the time between two same tasks
    @param description is the description of the task */
    public Task(String title, Date start, Date end, int interval, String description) {
        if (title != null && start != null && end != null) {
        
            final int secToMillisecond = 1000;
            this.title = title;
            this.time = start;
            this.start = start;
            this.end = end;
            this.interval = interval * secToMillisecond;
            this.active = false;
            this.description = description;
        }
    }

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
        return this.title;
    }

    /** Setup the title of the task
    @param title is the title of the task */
    public void setTitle(String title) {
        this.title = title;
    }

    /** Return the activity of the task
    @return the activity of the task */
    public boolean isActive() {
        return this.active;
    }

    /** Setup the activity of the task
    @param active is the activity of the task */
    public void setActive(boolean active) {
        this.active = active;
    }

    /** Return the start time of the task
    @return the start time of the repeated task
    or time of the non repeated task */
    public long getTime() {
         return this.time.getTime();
    }
    
    /** Setup the time of the non repeated task
    and make non repeated task for repeated task
    @param time is the time of the non repeated task */
    public void setTime(Date time) {

        this.time.setTime(time.getTime());
        this.start.setTime(time.getTime());
        this.end.setTime(time.getTime());
        this.interval = 0;
    }
    
    /** Setup the time of the non repeated task
    and make non repeated task for repeated task
    @param time is the time of the non repeated task */
    public void setTime(int time) {

        this.time.setTime(time);
        this.start.setTime(time);
        this.end.setTime(time);
        this.interval = 0;
    }

    /** Return the same as method getTime()
    @return the start time of the repeated task
    or time of the non repeated task */
    public long getStartTime() {

        return this.start.getTime();
    }
    
    /** Return the same as method getTime()
    @return the start time of the repeated task
    or time of the non repeated task */
    public Date getStartDate() {
         return this.start;
    }

    /** Return the end time of the repeated task
    or time of the non repeated task
    @return the end time of the repeated task
    or time of the non repeated task */
    public long getEndTime() {
        return this.end.getTime();
    }
    
    /** Return the end time of the repeated task
    or time of the non repeated task
    @return the end time of the repeated task
    or time of the non repeated task */
    public Date getEndDate() {

        return this.end;
    }

    /** Return the time between two same repeated tasks
    or 0 for the non repeated task
    @return the time between two same repeated tasks
    or 0 for the non repeated task */
    public int getRepeatInterval() {
        
        return this.interval;
    }


    /** Setup the start, end and interval of the repeated task
    or make the repeated task of the non repeated task
    @param start is the start time of the task
    @param end is the end of repeated period
    @param interval is the time between two same tasks  */
    public void setTime(Date start, Date end, int interval) {

        final int secToMillisecond = 1000;
        this.time.setTime(start.getTime());
        this.start.setTime(start.getTime());
        this.end.setTime(end.getTime());
        this.interval = interval * secToMillisecond;
    }
    
    /** Setup the start, end and interval of the repeated task
    or make the repeated task of the non repeated task
    @param start is the start time of the task
    @param end is the end of repeated period
    @param interval is the time between two same tasks  */
    public void setTime(int start, int end, int interval) {

        this.time.setTime(start);
        this.start.setTime(start);
        this.end.setTime(end);
        this.interval = interval;
    }

    /** Return true when it is the repeated task
    @return true when it is the repeated task */
    public boolean isRepeated() {
        return this.interval != 0;
    }

    /** Return earlier time when the task will happen after current time
    or -1 when it is impossible
    @param current is the same time
    @return earlier time when the task will happen after current time */
    public Date nextTimeAfter(Date current) {
        
        Date answerDate = new Date();
        if (this.time == null)
            throw new NullPointerException("Task can't be null");
            
        if (!(this.isActive()))
            answerDate = null;
        else {
            if (this.interval == 0) 
                if (this.time.after(current))
                    answerDate = this.time;
                else answerDate = null;      
            else {
                if (this.start.after(current)) answerDate = this.start;
                else {
                    if (this.start.equals(current)) {
                        long answer = this.interval
                            + this.start.getTime();
                            
                        answerDate.setTime(answer);
                            
                        if (this.end.before(answerDate))
                            answerDate = null;
                            
                    } else {
                        if (this.end.before(current)) 
                            answerDate = null;                
                        else {
                            long n = (current.getTime()
                            - this.start.getTime()) / this.interval;
                            long answer = (n + 1) * this.interval
                            + this.start.getTime();
                            
                            answerDate.setTime(answer);
                            
                            if (this.end.before(answerDate))
                                answerDate = null;
                        }
                    }
                }
            }
        }
        
        return answerDate;
        
    }
    
    /** Return earlier time when the task will happen after current time
    or -1 when it is impossible
    @param current is the same time
    @return earlier time when the task will happen after current time */
    public int nextTimeAfter(int current) {

        long answer;

        if (!(this.isActive()))
            answer = -1;
        else {
            if (this.interval == 0) 
                if (this.time.getTime() > current)
                    answer = this.time.getTime();
                else answer = -1;      
            else {
                if (this.start.getTime() > current)
                    answer = this.start.getTime();
                else {
                    if (this.start.getTime() == (long) current) answer = -1;
                    else {
                        if (this.end.getTime() < current) 
                            answer = -1;                
                        else {
                            long n = (current
                            - this.start.getTime()) / this.interval;
                            answer = (n + 1) * this.interval
                            + this.start.getTime();
                                 
                            if (this.end.getTime() < answer)
                                answer = -1;
                        }
                    }
                }
            }
        }
        return (int) answer;
    }

    /** Indicates whether some other object is "equal to" this one
    @return true if this object is the same as the obj argument;
    false otherwise.
    @param obj - the reference object with which to compare */
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (obj == null) return false;

        if (getClass() != obj.getClass()) return false;

        Task other = (Task) obj;

        return title.equals(other.title)
        && time.equals(other.time)
        && start.equals(other.start)
        && end.equals(other.end)
        && interval == other.interval
        && active == other.active
        && description.equals(other.description);
    }

    /** Returns a hash code value for the object
    @return a hash code value for this object */
    public int hashCode() {
        final int primeNumber1 = 7;
        final int primeNumber2 = 11;
        final int primeNumber3 = 13;
        final int primeNumber4 = 17;
        final int primeNumber5 = 19;
        final int primeNumber6 = 23;
        final int primeNumber7 = 29;

        return primeNumber1 * title.hashCode()
        + primeNumber2 * time.hashCode()
        + primeNumber3 * start.hashCode()
        + primeNumber4 * end.hashCode()
        + primeNumber5 * new Integer(interval).hashCode()
        + primeNumber6 * new Boolean(active).hashCode()
        + primeNumber7 * description.hashCode();
    }

    /** Returns a string representation of the object
    @return a string representation of the object */
    public String toString() {
        return getClass().getName()
        + "[title = " + title
        + ", time = " + time
        + ", start = " + start
        + ", end = " + end
        + ", interval = " + interval
        + ", active = " + active
        + ", description = " + description
        + "]";
    }

    /** Creates and returns a copy of this object
    @return a clone of this instance
    @throws CloneNotSupportedException - if the object's class does not
    support the Cloneable interface */
    public Task clone() throws CloneNotSupportedException {

        Task cloned = (Task) super.clone();

        cloned.time = (Date) this.time.clone();
        cloned.start = (Date) this.start.clone();
        cloned.end = (Date) this.end.clone();        

        return cloned;
    }
}
