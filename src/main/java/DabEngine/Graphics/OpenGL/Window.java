package DabEngine.Graphics.OpenGL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import DabEngine.Observer.*;

public class Window {

    private long win;
    private int width;
    private int height;
    private static int reswidth, resheight;
    private String title;
    private boolean fullscreen;
    private long monitor;
    private HashMap<Integer, Integer> hints;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private final String WINDOW_NOT_CREATED = "Window Not Created";
    private boolean loaded;

    public Window(int width, int height, String title, HashMap<Integer, Integer> hints, boolean fullscreen){
        this.width = width;
        this.height = height;
        this.title = title;
        this.hints = hints;
        this.fullscreen = fullscreen;
        
        if (!glfwInit()) {
            LOGGER.log(Level.SEVERE, "Window not initialized");
            System.exit(1);
        }
        
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
    	reswidth = vidmode.width();
    	resheight = vidmode.height();
        
        if(fullscreen) {
        	createFullScreenWindow(glfwGetPrimaryMonitor());
        }
        else {
        	createWindow();
        }
    }
    
    public void createWindow() {
    	for(Map.Entry<Integer, Integer> entry : hints.entrySet()) {
        	glfwWindowHint(entry.getKey(), entry.getValue());
        }
        
        win = glfwCreateWindow(width, height, title, 0, 0);
        if(win == GL_FALSE){
            LOGGER.log(Level.SEVERE, WINDOW_NOT_CREATED);
            glfwTerminate();
            System.exit(1);
        }
        glfwMakeContextCurrent(win);
        GL.createCapabilities();

        windowCallback();
        
        loaded = true;
    }
    
    public void createFullScreenWindow(long monitor) {
    	this.monitor = monitor;
    	
    	for(Map.Entry<Integer, Integer> entry : hints.entrySet()) {
        	glfwWindowHint(entry.getKey(), entry.getValue());
        }

        win = glfwCreateWindow(reswidth, resheight, title, monitor, 0);
        if(win == GL_FALSE){
            LOGGER.log(Level.SEVERE, WINDOW_NOT_CREATED);
            glfwTerminate();
            System.exit(1);
        }
        glfwMakeContextCurrent(win);
        GL.createCapabilities();

        windowCallback();
        
        loaded = true;
    }
    
    public void changeResolution(int dwidth, int dheight) {
    	glfwSetWindowSize(win, dwidth, dheight);
    }
    
    public boolean isLoaded() {
    	return loaded;
    }

    private void windowCallback(){
        glfwSetWindowSizeCallback(win, (long window, int argwidth, int argheight) -> {
            width = argwidth;
            height = argheight;
            EventManager.INSTANCE.submitEvent(new ResizeEvent());
        });
    }

    public long getWin(){
        return win;
    }
    
    public void setTitle(String title) {
    	glfwSetWindowTitle(win, title);
    }

    public void showWindow(){
        glfwShowWindow(win);
    }

    public int getWidth(){
    	if(fullscreen) {
    		GLFWVidMode vidmode = glfwGetVideoMode(monitor);
    		return vidmode.width();
    	}
    	else {
    		return width;
    	}
    }

    public int getHeight() {
    	if(fullscreen) {
    		GLFWVidMode vidmode = glfwGetVideoMode(monitor);
    		return vidmode.height();
    	}
    	else {
    		return height;
    	}
    }
}
