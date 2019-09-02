package com.multi.thread.callable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
/**
 * This class is a example of callable future implementation.
 * @author shra012
 *
 */
public class ParallelAdder {
	
	//The Odd number for which is the marks the of the addition. 
	private static final int LAST_ADDENDUM = 11;
	
	/**
	 * The method which uses the {@link Callable #java.util.concurrent.Callable} and {@link Future #java.util.concurrent.Future} types.
	 * To add all the numbers under the given odd number {@link #LAST_ADDENDUM}.
	 * @return the result sum.
	 */
	public Integer parallelSum()
	{                       
		long t1 = System.currentTimeMillis();
		ExecutorService executor = Executors.newFixedThreadPool(10);
		List <Future<Integer>> list = new ArrayList<Future<Integer>>();
		int count=0;
		int prev=0;                       
		for(int i=count;i<LAST_ADDENDUM;i++)
		{
			if(count%2 == 0 && count!=0)//grouping
			{
				System.out.println("Prev :" + prev + " current: " + i);
				Future<Integer> future = executor.submit(new CallableAdder(prev,i));
				list.add(future);
				count=1;                                              
				continue;
			}
			prev=i ;
			count++;                                   
		}
		int totsum=0;
		for(Future<Integer> fut : list)
		{
			try {
				totsum +=fut.get();
			} catch (InterruptedException | ExecutionException e) {                                               
				e.printStackTrace();
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}                       
		System.out.println("Parallel Sum is " + totsum);
		long t2 = System.currentTimeMillis();
		System.out.println("Time taken by parallelSum " + (t2-t1));
		executor.shutdown();
		return totsum;                       
	}
	
	/**
	 * Same job of addition as done by {@link #parallelSum()} but done sequentially
	 * @return the result sum.
	 */
	public int sequentialSum()
	{                      
		long t1 = System.currentTimeMillis();
		Integer totsum=0;                        
		for(int i=0;i<LAST_ADDENDUM;i++)
		{
			totsum=totsum+i;                                               
		}
		System.out.println("sequentialSum Total Sum is " + totsum);
		long t2 = System.currentTimeMillis();        
		System.out.println("Time taken by sequentialSum " + (t2-t1));
		return totsum;
	}
	
	/**
	 * Driver Main method to run both {@link #parallelSum()} and {@link #sequentialSum()}
	 * @param args
	 */
	public static void main(String[] args) {                       
		ParallelAdder adder = new ParallelAdder();
		int pSum= adder.parallelSum();
		int sSum= adder.sequentialSum();                       
		System.out.println("parallel Sum equals to Sequential Sum ? " );
		System.out.println("Answer is :: " + (pSum==sSum));      
	}
}
