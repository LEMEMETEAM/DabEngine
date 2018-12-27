package Graphics;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;

public class Window {

    private long win;
    private int width;
    private int height;
    private static int reswidth, resheight;
    private String title;
    private boolean fullscreen;
    private long monitor;
    private HashMap<Integer, Integer> hints;
    private boolean resized;
    
    static{
    	GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
    	reswidth = vidmode.width();
    	resheight = vidmode.height();
    }

    public Window(int width, int height, String title, HashMap<Integer, Integer> hints, boolean fullscreen){
        this.width = width;
        this.height = height;
        this.title = title;
        this.hints = hints;
        this.fullscreen = fullscreen;
    }
    
    public void createWindow() {
    	for(Map.Entry<Integer, Integer> entry : hints.entrySet()) {
        	glfwWindowHint(entry.getKey(), entry.getValue());
        }
        
        win = glfwCreateWindow(width, height, title, 0, 0);
        if(win == GL_FALSE){
            System.err.println("Window not created");
            glfwTerminate();
            System.exit(1);
        }
        glfwMakeContextCurrent(win);
        GL.createCapabilities();

        windowCallback();
    }
    
    public void createFullScreenWindow(long monitor) {
    	this.monitor = monitor;
    	
    	for(Map.Entry<Integer, Integer> entry : hints.entrySet()) {
        	glfwWindowHint(entry.getKey(), entry.getValue());
        }

        win = glfwCreateWindow(reswidth, resheight, title, monitor, 0);
        if(win == GL_FALSE){
            System.err.println("Window not created");
            glfwTerminate();
            System.exit(1);
        }
        glfwMakeContextCurrent(win);
        GL.createCapabilities();

        windowCallback();
    }
    
    public void changeResolution(int dwidth, int dheight) {
    	glfwSetWindowSize(win, dwidth, dheight);
    }

    private void windowCallback(){
        glfwSetWindowSizeCallback(win, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int argwidth, int argheight) {
                width = argwidth;
                height = argheight;
                resized = true;
            }
        });
    }

    public long getWin(){
        return win;
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

    public boolean isResized() {
        return resized;
    }
}
