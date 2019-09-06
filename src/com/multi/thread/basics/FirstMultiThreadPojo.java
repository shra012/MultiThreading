package com.multi.thread.basics;

public class FirstMultiThreadPojo {
	private volatile long value;
	private Object key = new Object();

	public FirstMultiThreadPojo(long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public void incrementValue() {
		synchronized(key) {
			this.value = this.value+1;
		}
	}

}
