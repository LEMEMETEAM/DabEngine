package Utils;

public class Timer  {
	
	private static boolean running;
	public static volatile int counter;
	
	public static void start() {
		running = true;
	}
	
	public static void update() {
		if(running)
			counter++;
	}
	
	public static void stop() {
		running = false;
		counter = 0;
	}
}
