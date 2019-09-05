package com.multi.thread.waitandnotify;

public class WaitNotifyRunner {

	private static Object key = new Object();
	private static int[] buffer;
	private static Integer count;

	static class Consumer {

		void consume() {
			synchronized (key) {
				while (isEmpty()) {
					try {
						key.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				buffer[--count] = 0;
				key.notify();
			}
		}

		public boolean isEmpty() {
			return count == 0;
		}
	}

	static class Producer {

		void produce() {
			synchronized (key) {
				while (isFull()) {
					try {
						key.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				buffer[count++] = 1;
				key.notify();
			}
		}

		public boolean isFull() {
			return count == buffer.length;

		}
		
		public boolean isEmpty() {
			return count == 0;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		buffer = new int[10];
		count = 0;
		Producer producer = new Producer();
		Consumer consumer = new Consumer();
		Runnable produce = () -> {
			for (int i = 0; i < 1500; i++)
				producer.produce();
			System.out.println("Done producing");
		};
		Runnable consume = () -> {
			for (int i = 0; i < 1300; i++)
				consumer.consume();
			System.out.println("Done consuming");
		};
		Thread producerWorker = new Thread(produce);
		Thread consumerWorker = new Thread(consume);
		producerWorker.start();
		consumerWorker.start();
		//consumerWorker.join();
		Runnable checker = () -> {
			System.out.println("Lanched Delayed Consumer");
			for (int i = 0; i < 200; i++)
				consumer.consume();
		};
		Thread delayedConsumer = new Thread(checker);
		delayedConsumer.start();
		producerWorker.join();
		System.out.println("Data in Buffer " + count);
	}

}
