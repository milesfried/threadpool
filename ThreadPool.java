/**
 * A simple thread pool API.
 * 
 * Tasks that wish to get run by the thread pool must implement the
 * java.lang.Runnable interface.
 */
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;


public class ThreadPool
{
	LinkedBlockingQueue queue;
	ThreadRunner[] threads;
	/**
	 * Create a default size thread pool.
 	 */
	public ThreadPool() {
		threads = new ThreadRunner[5];
		queue = new LinkedBlockingQueue();
		for (int i = 0; i < 5; i++){
			threads[i] = (new ThreadRunner());
			threads[i].start();
		}
		
    }
	
	
	/**
	 * Create a thread pool with a specified size.
	 * 
	 * @param int size The number of threads in the pool.
	 */
	public ThreadPool(int size) {
		threads = new ThreadRunner[size];
		queue = new LinkedBlockingQueue();

		for (int i = 0; i < size; i++){
			threads[i] = new ThreadRunner();
			threads[i].start();
		}
    }
	
	
	/**
	 * shut down the pool.
	 */
	public void shutdown() {
	for (int i = 0; i < threads.length; i++){
		threads[i].interrupt();	
	}
	}

	
	/**
	 * Add work to the queue.
	 */
	public void add(Runnable run) {
		synchronized(queue) {
			queue.add(run);
			queue.notify();
		}
	}
		


		private class ThreadRunner extends Thread{
			Task task;
			public void doWork() throws InterruptedException {
				while(true){
					synchronized(queue){
						while(queue.isEmpty()){
							try{
								queue.wait();
							}
							catch(InterruptedException ie){
								throw ie;
							}
						}
						task = (Task) queue.poll();
					}
					try{
					task.run();
					} catch (RuntimeException e){
						e.printStackTrace();
					}
					}
			}
			public void run() {
				try {
					while (!Thread.currentThread().isInterrupted()) {
						doWork();
					}
				}
				catch (InterruptedException ie) {
					// caught exception thrown from doWork()
				}
			}
			}
		}
	
