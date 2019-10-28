package DabEngine.Utils;

import static org.lwjgl.glfw.GLFW.*;

public class Timer  {

	private double lasttime = 0.0;
	private double deltaTime = 0.0;

	public Timer()
	{
		lasttime = getTime();
	}
	
	/**
	 * Get the time in seconds
	 * 
	 * @return The system time in seconds
	 */
	public double getTime() {
		return System.nanoTime() / 1000000000.0;
	}

	public void update(){
		double time = getTime();
		deltaTime = time - lasttime;
		lasttime = time;
	}

	public double getDelta() {
		return deltaTime;
	}

}
