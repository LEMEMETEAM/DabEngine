package DabEngine.Utils;

import static org.lwjgl.glfw.GLFW.*;

public class Timer  {

	private static double lasttime = 0.0;
	private static double deltaTime = 0.0;
	
	/**
	 * Get the time in seconds
	 * 
	 * @return The system time in seconds
	 */
	public static double getTime() {
		return System.nanoTime() / 1000000000.0;
	}

	public static void update(){
		double time = getTime();
		deltaTime = time - lasttime;
		lasttime = time;
	}

	public static double getDelta() {
		return deltaTime;
	}

	public static void init(){
		lasttime = getTime();
	}
}
