package com.multi.thread.sleepandinterrupt;

import static java.lang.Thread.sleep;

/**
 * 
 * @author shra012
 *
 */
public class SleepAndInterruptPojo {
	private Object key = new Object();

	/**
	 * Method a requires a lock on key
	 * @throws InterruptedException 
	 */
	public void a() throws InterruptedException {
		if(Thread.currentThread().getName().equals("Second"))
			return;
		synchronized(key) {
			sleep(100000);
		}
	}

	/**
	 * Method b when called by another thread, if a
	 * thread sleeping at {@link #a()} exists, it will be interrupted
	 * before accessing a().
	 * @throws InterruptedException 
	 */
	public void b() throws InterruptedException {
		for (Thread t : Thread.getAllStackTraces().keySet()) {
			if (t.getName().equals("First")) {
				t.interrupt();
			}
		}
		
		a();
	}
}

