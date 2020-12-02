package scheduler;


public class WorkFactory {
	private int jobCount = 0;
	public JobWorkable createWork() {
		JobWorkable worker = new JobWorker();
		return worker;
		/* 
		 * Return a new instance of a class that implements JobWorkable.
		 * You should provide that class.  After running your program
         * as you submit it, I will have this method return an instance
         * of a class that I have created (which will also implement
         * JobWorkable.)
		 */
	}
}
