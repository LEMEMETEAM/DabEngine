package DabEngine.Utils;

public class Timer  {
	
	private static boolean running;
	public static volatile int counter;
	
	public void start() {
		running = true;
	}
	
	public static void update() {
		if(running)
			counter++;
	}
	
	public void stop() {
		running = false;
		counter = 0;
	}
}
