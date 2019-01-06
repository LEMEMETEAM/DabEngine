package Utils;

public class Timer {
	
	private static long lastTime = System.nanoTime();
	
	private Timer() {}
	
	public static void setLastTime(long newlastTime) {
		lastTime = newlastTime;
	}
	
	public static long getLastTime() {
		return lastTime;
	}
	
	public static long getCurrentTime() {
		// TODO Auto-generated method stub
		return System.nanoTime();
	}
	
	public static double SecondsToNano(double seconds) {
		return seconds * Math.pow(10, 9);
	}
	
	public static double SecondsToNano(int seconds) {
		return seconds * Math.pow(10, 9);
	}
}
