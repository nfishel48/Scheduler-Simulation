package scheduler;

import java.util.LinkedList;
import java.util.Queue;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

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
  public synchronized void add(Job J) {
    ready.add(J);
    System.out.println("added job"+J+"to the readyQ");
    notifyAll();
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
      currentlyRunningJob = ready.remove();
      currentlyRunningJob.start();
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
  public synchronized void blockTilThereIsAJob() {
    if (hasRunningJob())
      return;
    while (ready.peek() == null) {
      try {
        System.out.println("Kernal Is waiting for a job");
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    notify();
	  System.out.println("evidently there is now a job on readyQ");
  }
}
  

