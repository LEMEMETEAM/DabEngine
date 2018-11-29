package Graphics;

import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;

public class Window {

    private long win;
    private int width;
    private int height;
    private boolean resized;

    public Window(int width, int height, String title){
        this.width = width;
        this.height = height;

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

    public Window(int width, int height, String title, int monitor){
        this.width = width;
        this.height = height;

        win = glfwCreateWindow(width, height, title, monitor, 0);
        if(win == GL_FALSE){
            System.err.println("Window not created");
            glfwTerminate();
            System.exit(1);
        }
        glfwMakeContextCurrent(win);
        GL.createCapabilities();

        windowCallback();
    }

    public Window(int width, int height, String title, int monitor, int share){
        this.width = width;
        this.height = height;

        win = glfwCreateWindow(width, height, title, monitor, share);
        if(win == GL_FALSE){
            System.err.println("Window not created");
            glfwTerminate();
            System.exit(1);
        }
        glfwMakeContextCurrent(win);
        GL.createCapabilities();

        windowCallback();
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
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isResized() {
        return resized;
    }
}
