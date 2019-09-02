package com.multi.thread.sleepandwait;

public class SleepAndInterruptExample {
	public static void main(String[] args) {
		SleepAndInterruptPojo sleepAndInterruptPojo = new SleepAndInterruptPojo();
		Runnable run1 = ()->{
			try {
				sleepAndInterruptPojo.a();
			} catch (InterruptedException e) {
				System.out.println("Thread "+Thread.currentThread().getName()+" is interrupted");
			}
		};
		
		Runnable run2 = ()->{
			try {
				sleepAndInterruptPojo.b();
			} catch (InterruptedException e) {
				System.out.println("Thread "+Thread.currentThread().getName()+" is interrupted");
			}
		};
		
		Thread first = new Thread(run1,"First");
		Thread second = new Thread(run2,"Second");
		
		first.start();
		second.start();
	}
}
