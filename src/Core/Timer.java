package Core;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.glViewport;

public class Timer implements Runnable {
	
	private Thread t;
	private boolean running;
	private Engine engine;
	
	public Timer(Engine engine) {
		this.engine = engine;
	}
	
	public void start() {
		t = new Thread(this, "Timer");
		t.start();
	}
	
	public void stop() {
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
