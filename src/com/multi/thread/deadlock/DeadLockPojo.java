package com.multi.thread.deadlock;

/**
 * 
 * @author shra012
 * This class is to demonstrate the dead lock scenario in java.
 */
public class DeadLockPojo {
	private Object key1 = new Object();
	private Object key2 = new Object();
	/**
	 * Method a requires a lock on key1
	 */
	public void a() {
		synchronized(key1) {
			System.out.println("Thread "+Thread.currentThread().getName()+" is executing method a");
			b();
		}
	}
	
	/**
	 * Method b requires a lock on key2
	 */
	public void b() {
		synchronized(key2) {
			System.out.println("Thread "+Thread.currentThread().getName()+" is executing method b");
			c();
		}
	}
	
	/**
	 * Method c requires a lock on key1
	 */	
	public void c() {
		synchronized(key1) {
			System.out.println("Thread "+Thread.currentThread().getName()+" is executing method c");
		}
	}
}
