package DabEngine.Core;

import java.util.HashMap;

import org.lwjgl.glfw.GLFWVidMode;

import DabEngine.Graphics.OpenGL.Viewport.Viewport;

/**
 * Abstract class that every main game class must extend
 */
public abstract class App {
	
	public int WIDTH, HEIGHT;
	public String TITLE;
	public HashMap<Integer, Integer> hints = new HashMap<>();
	public boolean MAXIMISED;
	public long currentMonitor;
	public GLFWVidMode currentVidMode;
	public boolean fullscreenOnMaximise;
	public Viewport vp;
	
	/**
	 * Main render method
	 */
	public abstract void render();
	/**
	 * Main update method
	 */
	public abstract void update();
	/**
	 * Initialization method 
	 * (anything related to opengl must be initialized here since the context has not been made at the point at which this is run)
	 */
	public abstract void init();
	/**
	 * Base resize method which handles the reszing of the window.
	 * @param width
	 * @param height
	 */
	public abstract void resize(int width, int height);
}
