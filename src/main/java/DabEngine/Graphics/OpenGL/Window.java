package DabEngine.Graphics.OpenGL;

import static org.lwjgl.glfw.GLFW.*;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVidMode.Buffer;
import org.lwjgl.opengl.GL;

import DabEngine.Core.App;
import DabEngine.Core.IDisposable;

public class Window implements IDisposable{

    private long win;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private boolean loaded;
    private App app;
    private LinkedHashMap<Long, Buffer> monitors = new LinkedHashMap<>();

    public Window(App app){
        
        if (!glfwInit()) {
            LOGGER.log(Level.SEVERE, "Window not initialized");
            System.exit(1);
        }
        
        this.app = app;
        
        PointerBuffer ms = glfwGetMonitors();
        for(int i = 0; i < ms.limit(); i++){
            long m = ms.get(i);
            monitors.put(m, glfwGetVideoModes(m));
        }

        Entry<Long, Buffer> primary = monitors.entrySet().stream().findFirst().get();

        if(app.MAXIMISED) {
            glfwWindowHint(GLFW_RED_BITS, primary.getValue().get(0).redBits());
            glfwWindowHint(GLFW_GREEN_BITS, primary.getValue().get(0).greenBits());
            glfwWindowHint(GLFW_BLUE_BITS, primary.getValue().get(0).blueBits());
            glfwWindowHint(GLFW_REFRESH_RATE, primary.getValue().get(0).refreshRate());
            win = glfwCreateWindow(primary.getValue().get(0).width(), primary.getValue().get(0).height(), app.TITLE, primary.getKey(), 0);
        }
        else{
            for(Entry<Integer, Integer> hints : app.hints.entrySet()){
                glfwWindowHint(hints.getKey(), hints.getValue());
            }
            win = glfwCreateWindow(app.WIDTH, app.HEIGHT, app.TITLE, 0, 0);
        }

        loaded = true;

        glfwMakeContextCurrent(win);
        GL.createCapabilities();

        glfwSetFramebufferSizeCallback(win, (long window, int argwidth, int argheight) -> {
            app.resize(argwidth, argheight);
            app.WIDTH = argwidth;
            app.HEIGHT = argheight;
            app.render();
            glfwSwapBuffers(win);
        });

        glfwSetWindowMaximizeCallback(win, (long window, boolean maximised) -> {
            if(app.fullscreenOnMaximise){
                if(maximised){
                    app.MAXIMISED = true;
                    glfwSetWindowMonitor(win, app.currentMonitor, 0, 0, app.currentVidMode.width(), app.currentVidMode.height(), app.currentVidMode.refreshRate());
                }
                else{
                    app.MAXIMISED = false;
                    glfwSetWindowMonitor(win, 0, 0, 0, app.WIDTH, app.HEIGHT, GLFW_DONT_CARE);
                }
            }
        });
    }
    
    public void changeResolution(int dwidth, int dheight) {
    	glfwSetWindowSize(win, dwidth, dheight);
    }
    
    public boolean isLoaded() {
    	return loaded;
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

    @Override
    public void dispose() {
        glfwDestroyWindow(win);
    }
}
