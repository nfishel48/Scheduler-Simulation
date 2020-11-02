package scheduler;

public class JobWorker implements JobWorkable{
    public void doWork(){
         // getting the current thread 's name. 
         System.out.println("Fetching current thread name.."); 
         System.out.println(Thread.currentThread().getName()); 
        
    }
}
