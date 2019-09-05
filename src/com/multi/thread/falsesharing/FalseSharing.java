package com.multi.thread.falsesharing;

public class FalseSharing {
	public static int NUM_THREADS_MAX = 4;
	public final static long ITERATIONS = 50_000_000L;

	private static VolatileLongPadded[] paddedLongs;
	private static VolatileLongUnPadded[] unPaddedLongs;

	public final static class VolatileLongPadded{
		public long q1,q2,q3,q4,q5,q6;
		public volatile long value = 0L;
		public long q11,q12,q13,q14,q15,q16;
	}

	public final static class VolatileLongUnPadded{
		public volatile long value = 0L;
	}
	
	static {
		paddedLongs = new VolatileLongPadded[NUM_THREADS_MAX];
		for (int i = 0; i < paddedLongs.length; i++) {
			paddedLongs[i] = new VolatileLongPadded();
		}
		unPaddedLongs = new VolatileLongUnPadded[NUM_THREADS_MAX];
		for (int i = 0; i < unPaddedLongs.length; i++) {
			unPaddedLongs[i] = new VolatileLongUnPadded();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		runBenchMark();
	}
	
	private static void runBenchMark() throws InterruptedException {
		long begin,end;
		for (int i = 0; i < NUM_THREADS_MAX; i++) {
			Thread[] threads = new Thread[i];
			for (int j = 0; j < threads.length; j++) {
				threads[j] = new Thread(createPaddedRunnable(j));
			}
			begin = System.currentTimeMillis();
			for (Thread thread : threads) {thread.start();}
			for (Thread thread : threads) {thread.join();}
			end = System.currentTimeMillis();
			System.out.printf("Padded # threads %d - T =%dms\n",i+1,end-begin);
			
			for (int j = 0; j < threads.length; j++) {
				threads[j] = new Thread(createUnPaddedRunnable(j));
			}
			begin = System.currentTimeMillis();
			for (Thread thread : threads) {thread.start();}
			for (Thread thread : threads) {thread.join();}
			end = System.currentTimeMillis();
			System.out.printf("UnPadded # threads %d - T =%dms\n",i+1,end-begin);
			
		}
	}
	
	private static Runnable createPaddedRunnable(final int i) {
		return ()->{
			long value = ITERATIONS+1;
			while(0 != --value) {
				paddedLongs[i].value = value;
			}
		};
	}
	
	
	private static Runnable createUnPaddedRunnable(final int i) {
		return ()->{
			long value = ITERATIONS+1;
			while(0 != --value) {
				unPaddedLongs[i].value = value;
			}
		};
	}
}
