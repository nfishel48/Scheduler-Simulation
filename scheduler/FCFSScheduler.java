package scheduler;

import java.util.LinkedList;
import java.util.Queue;

/**
 * <p>Title: FCFSScheduler</p>
 * <p>Description: Component of the simulate operating system that encapsulates FCFS job scheduling.</p>
 * <p>Copyright: Copyright (c) 2015, 2004</p>
 * <p>Company: </p>
 * @author Matt Evett
 * @version 2.0
 */

import java.util.concurrent.ConcurrentLinkedQueue;

public class FCFSScheduler extends Scheduler {

  Queue<Job> ready = new LinkedList();

  /*
   * return true if the scheduler has jobs in any queues (ready, or IO queues [in
   * later version of project]
   */
  public boolean hasJobsQueued() {
    if (ready.peek() == null) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Add new job to readyQ. This should interract with blockTilThereIsAJob
   * appropriately. This method will probably be executed by the Submittor thread,
   * while blockTilThereIsAJob will probably be executed by the kernel
   * (SystemSimulator) thread.
   */
  public void add(Job J) {

  }

  /**
   * Remove job from readyQ. No need to synchronize, as no thread blocks on "full"
   * buffer
   */
  public void remove(Job J) {

  }

  /**
   * If the ready queue is empty, return false. Otherwise, start the next job in
   * the queue, returning true. If the queue is empty return false. Make the next
   * job in the ready queue run. You should probably invoke Thread.start() on it.
   */
  public boolean makeRun() {
    if (ready.peek() == null) {
      return false;
    } else {
      Job currentJob = ready.remove();
      currentJob.start();
      return true;
    }
  }

  /**
   * blockTilThereIsAJob() Invoked by OS simulator when it wants to get a new Job
   * to run. Will block if the ready queue is empty until a Job is added to the
   * queue.
   * 
   * 
   */
  public void blockTilThereIsAJob() {
    if (hasRunningJob())
      return;
    System.out.println("TO_DO: blockTilThereIsAJob not yet implemented");
    while (ready.peek() == null) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
	  System.out.println("evidently there is now a job on readyQ");
  }
}
  

