package DabEngine.Utils;

import static org.lwjgl.glfw.GLFW.*;

public class Timer  {

	private static double lasttime = getTime();
	private static double deltaTime = 0.0;
	
	/**
	 * Get the time in milliseconds
	 * 
	 * @return The system time in milliseconds
	 */
	public static double getTime() {
		return glfwGetTime();
	}

	public static void update(){
		double time = getTime();
		deltaTime = time - lasttime;
		lasttime = time;
	}

	public static double getDelta() {
		return deltaTime;
	}
}
