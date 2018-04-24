package model;
 import java.util.Iterator;
 import java.util.NoSuchElementException;
 import org.apache.log4j.Logger;

/** The list of task as array*/
public class ArrayTaskList extends TaskList {

    private static final Logger log = Logger.getLogger(ArrayTaskList.class);
    
    protected Task[] arrayTask = new Task[10];
    private int realSize = 0;
    
    /** Adding new task to the TaskList
    @param task is new task for adding 
    @throw NullPointerException means task is null */
    public void add(Task task) {
        if (task == null)
            throw new NullPointerException("Task can't be null");
        
        realSize ++;
        
        if (arrayTask.length < realSize) {
            Task[] arrayTaskold = arrayTask;
            arrayTask = new Task[arrayTask.length * 2];
            System.arraycopy(arrayTaskold, 0, arrayTask, 0, arrayTaskold.length);
        }
        arrayTask[realSize - 1] = task;
    }

    /** Removing new task from the TaskList
    @param task is the task for removing
    @return true if find the task for removing 
    @throw NullPointerException means task is null */
    public boolean remove(Task task) {
            if (task == null)
                throw new NullPointerException("Task can't be null");

            for (int i = 0; i < arrayTask.length; i++) {
                if (arrayTask[i].equals(task)) {
                    Task[] arrayTaskold = arrayTask;
                    arrayTask = new Task[arrayTask.length - 1];

                        System.arraycopy(arrayTaskold, 0, arrayTask, 0, i);

                       if (arrayTask.length != i) {
                           System.arraycopy(arrayTaskold, i + 1, arrayTask, i, arrayTask.length - 1 - i);
                       }

                    realSize --;
                    return true;
                }
            }
            return false;
    }

    /** Return size of the TaskList
    @return size of the TaskList */
    public int size() {
        return realSize;
    }

    /** Return the task with index
    @param index give the task index in the TaskList
    @return give the task with index 
    @throw IndexOutOfBoundsException means Illegal index */
    public Task getTask(int index) {
        if (index < 0 || index >= this.size())
            throw new IndexOutOfBoundsException("Illegal index");
    
        return arrayTask[index];
    }
   
    /** Returns an iterator over elements of type Task    
    @return costructor of inner class IteratorArray */
    public Iterator<Task> iterator() {
        return new IteratorArray();
    }  
    
    /** An iterator over a collection ArrayTaskList */
    public class IteratorArray implements Iterator<Task> {
        private Task iteratorTask;
        private int iteratorNumb;
        private int removeVariable = -1;

        /** costructor of inner class IteratorArray */
        public IteratorArray() {
            this.iteratorNumb = 0;
            this.iteratorTask = ArrayTaskList.this.getTask(this.iteratorNumb);
            this.removeVariable = -1; 
        }

        /** Returns true if the iteration has more elements        
        @return true if the iteration has more elements */
        public boolean hasNext() {
            return this.iteratorNumb < ArrayTaskList.this.size();
        }

        /** Returns the next element in the iteration
        @return the next element in the iteration 
        @throw NoSuchElementException - the iteration has no more elements */
        public Task next() {
            if (this.hasNext()) {
              this.iteratorTask = ArrayTaskList.this.getTask(this.iteratorNumb);
              this.removeVariable = this.iteratorNumb;
              this.iteratorNumb++;
              return this.iteratorTask;
            }
            throw new NoSuchElementException();
        }
        
        /** Removes from the underlying collection the last element 
        returned by this iterator
        @throw IllegalStateException - if the next method has not yet 
        been called, or the remove method has already been called after 
        the last call to the next method */
        public void remove() {            
            
            if (this.removeVariable == -1) 
                throw new IllegalStateException(); 
             
                Task[] arrayTaskold = ArrayTaskList.this.arrayTask;
                ArrayTaskList.this.arrayTask = 
                    new Task[ArrayTaskList.this.size() - 1];
                
                for (int j = 0; j < this.iteratorNumb - 1; j++) {
                    ArrayTaskList.this.arrayTask[j] = arrayTaskold[j];
                }
                
                for (int j = this.iteratorNumb - 1; 
                    j <  ArrayTaskList.this.size(); j++) {
                    ArrayTaskList.this.arrayTask[j] = arrayTaskold[j + 1];
                }
                this.removeVariable = -1;
                this.iteratorNumb--;
            
                       
        }
    }
    
    /** Indicates whether some other object is "equal to" this one 
    @return true if this object is the same as the obj argument;
    false otherwise. 
    @param obj - the reference object with which to compare */
    public boolean equals(Object obj) {
    
        if (this == obj) return true;
        
        if (obj == null) return false;
        
        if (getClass() != obj.getClass()) return false;
        
        ArrayTaskList other = (ArrayTaskList) obj;
        
        if (this.size() != other.size()) return false;
        
        int i = 0;
        for (Task task : this) {            
            if (task.equals(other.getTask(i))) {                
                if (i == this.size() - 1) return true;
                i++;
                continue;
            } else return false;            
        } 
        
        return false;
    }

     /** Returns a hash code value for the object
    @return a hash code value for this object */
    public int hashCode() {
        final int primeNumber1 = 7;
        final int primeNumber2 = 11;
        
        int hash = primeNumber1;
        
        for (Task task : this) {
            hash = primeNumber2 * hash + task.hashCode();
        }
    
        return hash;
    }
    
    /** Returns a string representation of the object
    @return a string representation of the object */
    public String toString() {
        String bigString = getClass().getName() + "[";

        if (this.size()>0) {
            for (Task task : this) {
                bigString = bigString + task.toString();
            }
        }
    
        return bigString + "]";
    }
    
    /** Creates and returns a copy of this object
    @return a clone of this instance
    @throws CloneNotSupportedException - if the object's class does not 
    support the Cloneable interface */
    public TaskList clone() throws CloneNotSupportedException {
    
        TaskList cloned = (TaskList) super.clone();
        
        for (Task task : cloned) 
            task = task.clone();
                
        return cloned;
    }
}