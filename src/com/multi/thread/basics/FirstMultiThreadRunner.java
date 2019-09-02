package com.multi.thread.basics;

import java.util.ArrayList;
import java.util.Arrays;

public class FirstMultiThreadRunner {
	public static void main(String[] args) throws InterruptedException {
		FirstMultiThreadRunner runner = new FirstMultiThreadRunner();
		
		FirstMultiThreadPojo multiThreadPojo = new FirstMultiThreadPojo(0L);

		Runnable run = () ->{
			//System.out.println("Started Thread "+Thread.currentThread().getName());
			for(int i=0;i<1_000;i++) {
				multiThreadPojo.incrementValue();
			}
		};
		runner.manuallyCreateThreads(run);
		runner.createThreadsUsingFolkJointPool(run);
		System.out.println("The value in the Pojo is :"+multiThreadPojo.getValue());
	}
	/**
	 * This method is used to call the folk join pool for multi threading
	 * @param run Runnable to be passed to do the multi-threading task
	 */
	public void createThreadsUsingFolkJointPool(Runnable run){
		long start = System.currentTimeMillis();
		Thread[] threads = new Thread[1_000];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(run,"Thread"+i);
		}
		
		ArrayList<Thread> threadList = new ArrayList<Thread>(Arrays.asList(threads));
		
		
		threadList.parallelStream().forEach(thread ->{
			thread.start();
		});
		
		threadList.parallelStream().forEach(thread->{
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		long end  = System.currentTimeMillis();
		long totalTimeTaken = end-start;
		System.out.println("Total Time Elapsed in milli seconds for Folk Join Pool "+totalTimeTaken);
	}
	
	/**
	 * This method is used to create threads for multi-threading
	 * @param run Runnable to be passed to do the multi-threading task
	 * @throws InterruptedException
	 */
	public void manuallyCreateThreads(Runnable run) throws InterruptedException{
		long start = System.currentTimeMillis();
		Thread[] threads = new Thread[1_000];

		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(run,"Thread"+i);
			threads[i].start();
		}

		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}
		long end  = System.currentTimeMillis();
		long totalTimeTaken = end-start;
		//long totalTimeInSeconds = totalTimeTaken/1000;
		System.out.println("Total Time Elapsed in milli seconds for Manual Thread Creation "+totalTimeTaken);
		//System.out.println("Total Time Elapsed in seconds "+totalTimeInSeconds);
	}
}
