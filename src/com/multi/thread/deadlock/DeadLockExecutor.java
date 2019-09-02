package com.multi.thread.deadlock;

public class DeadLockExecutor {
	public static void main(String[] args) {
		DeadLockPojo deadLockPojo = new DeadLockPojo();
		Runnable run1 = ()->{
			deadLockPojo.a();
		};
		
		Runnable run2 = ()->{
			deadLockPojo.b();
		};
		
		Thread first = new Thread(run1,"First");
		Thread second = new Thread(run2,"Second");
		
		first.start();
		second.start();
		System.out.println("Hi");
	}
}
