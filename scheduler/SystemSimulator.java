package scheduler;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


class SystemSimulator extends Thread {
  private static final int ILLEGAL_TERMINATION = -20;
  private final Scheduler myScheduler; // scheduler for jobs
  private volatile boolean jobsRemainToBeSubmitted = true; // true if not all jobs have yet been submitted
  private final ReentrantLock singleThreadMutex; // Used to guarantee that only
  public Condition done;

  // Used to store information to create a Gannt chart
  private final GanntChart chart = new GanntChart();

  public ReentrantLock getSingleThreadMutex() {
    return singleThreadMutex;
  }

  /**
   * SystemSimulator() constructor is private to force the use of other
   * constructors.
   */
  @SuppressWarnings("unused")
  private SystemSimulator() {
    myScheduler = null;
    singleThreadMutex = null;
  }

  public SystemSimulator(Scheduler s) {
    singleThreadMutex = new ReentrantLock();
    myScheduler = s;
    done = singleThreadMutex.newCondition();
  }

  /*
   * The basic structure of this method is straightforward: the simulator sits in
   * a loop, sleeping. The simulator awakens only when it is interrupted
   * ("poked").
   */

  public void run() {
    long currentIdleTimeStart; // Wall time when current idle period started
    long currentIdleTimeEnd; // Wall time when current idle period ended

    // The OS thread should be started before any Job thread so it can gain this
    // lock first
    singleThreadMutex.lock();
    chart.start();

    // loop while we have jobs left to be scheduled or scheduler has jobs scheduled
    while (jobsRemainToBeSubmitted || myScheduler.hasJobs()) {
      /*
       * If readyQ is empty and no job running, OS will block on readyQ, submittor
       * will signal it. If there's a running Job, TimeSlice will interrupt that, not
       * the OS.
       */

      currentIdleTimeStart = System.currentTimeMillis(); // start idle timer
      // If there are no jobs to schedule, block on readyQ, waiting on Submittor to
      // signal it.
      myScheduler.blockTilThereIsAJob();
      currentIdleTimeEnd = System.currentTimeMillis(); // end idle timer
      if (currentIdleTimeEnd > currentIdleTimeStart)
        chart.recordEvent(currentIdleTimeStart, currentIdleTimeEnd, "IDLE");
      System.out.println("Found a Job");

      System.out.println("Running a job");
      myScheduler.makeRun(); // the next Job should start running but immediately block on OS mutex lock

      try {
        done.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      //Should get to here when that Job completes (calls Exit).
      System.out.println("Job complete!");
    } // exit loop, we have no jobs left and none scheduled
    chart.end();
    // print gannt chart
    chart.print();
  }
  
  /*
   * adds given job, j, to the ready set. Invoked by a Submittor.
   * Keep in mind that j might not start running immediately, depending on whether another job is already running.
   */

  public void AddNewProcess(String name, String burstDescription, JobWorkable workToDo)
  {
	  Job newJob = new Job(burstDescription, this, name, workToDo);
	  myScheduler.add( newJob );
  }
  
  /**
   * Exit() called by a Job thread to indicate that it is terminating.
   * This should be the last instruction executed by a Job's run method.
   * This method is meant to mimic a true system call to exit().
   * Note that because this method will be invoked by Job, a Thread,
   * we can use the Thread.getCurrentThread() method to get a reference to the Job that is invoking this method.
   *
   * @param jobStart = wall time when Job first started running
   */
  public void exit()
  {
	  // remove job from scheduler, record data into gannt chart
	  Job terminatingJob = (Job)Thread.currentThread(); // reference to calling thread
	  Job schedulersRunning = myScheduler.getRunningJob();
	  /*
	   * If all is going well, terminatingJob and schedulersRunning should be equal.
	   * I've left both here for demonstration purposes--really only need one.
	   */
	  
	  // store job gannt data
	  chart.recordEvent( terminatingJob.getStartTime(), System.currentTimeMillis(), terminatingJob.getNameOf() );
	  myScheduler.clearRunningJob(); // remove job from array list
	  terminatingJob.getMyCondition().signal(); // This should release the OS to do its thing
	  singleThreadMutex.unlock(); // N.B. this code is only executed by a Job thread
  }

  /*
   * public noMoreJobsToSubmit() called by the Submittor when the last Job has been submitted.
   * The simulator should use this information to eventually terminate when all Jobs have finished.
   */
  public void noMoreJobsToSubmit()
  {
	  jobsRemainToBeSubmitted = false;
  }


}
