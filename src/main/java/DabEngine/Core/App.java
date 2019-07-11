package DabEngine.Core;

import java.util.HashMap;

import org.lwjgl.glfw.GLFWVidMode;

import DabEngine.Graphics.OpenGL.Viewport.Viewport;

public abstract class App {
	
	public int WIDTH, HEIGHT;
	public String TITLE;
	public HashMap<Integer, Integer> hints = new HashMap<>();
	public boolean MAXIMISED;
	public long currentMonitor;
	public GLFWVidMode currentVidMode;
	public boolean fullscreenOnMaximise;
	public Viewport vp;
	
	public abstract void render();
	public abstract void update();
	public abstract void init();
	public abstract void resize(int width, int height);
}
