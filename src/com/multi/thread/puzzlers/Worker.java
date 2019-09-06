package com.multi.thread.puzzlers;

import java.util.Timer;
import java.util.TimerTask;

public class Worker extends Thread {
	private volatile boolean quttingTime = false;
	private Object key = new Object();
	@Override
	public void run() {
		while(!quttingTime) {
			working();
			System.out.println("Still Working...");
		}
		System.out.println("Time To Quit!");
	}

	private void working() {
		try {
			sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	synchronized void quit() throws InterruptedException {
		synchronized(key) {
			quttingTime = true;
			join();
		}
	}

	synchronized void keepWorking() {
		synchronized(key) {
			quttingTime=false;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		final Worker worker = new Worker();
		worker.start();

		Timer t = new Timer(true);
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				worker.keepWorking();
			}
		}, 500);

		sleep(400);
		worker.quit();
	}


}
