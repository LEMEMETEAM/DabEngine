package DabEngine.Graphics.OpenGL;

import static org.lwjgl.glfw.GLFW.GLFW_BLUE_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_DONT_CARE;
import static org.lwjgl.glfw.GLFW.GLFW_GREEN_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_DEBUG_CONTEXT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RED_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_REFRESH_RATE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetMonitors;
import static org.lwjgl.glfw.GLFW.glfwGetVideoModes;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMaximizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
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

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);
        
        PointerBuffer ms = glfwGetMonitors();
        for(int i = 0; i < ms.limit(); i++){
            long m = ms.get(i);
            monitors.put(m, glfwGetVideoModes(m));
        }

        Optional<Entry<Long, Buffer>> op = monitors.entrySet().stream().findFirst();
        Entry<Long, Buffer> primary = null;
        

        if(app.MAXIMISED) {
            if(op.isPresent()){
                primary = op.get();
                glfwWindowHint(GLFW_RED_BITS, primary.getValue().get(0).redBits());
                glfwWindowHint(GLFW_GREEN_BITS, primary.getValue().get(0).greenBits());
                glfwWindowHint(GLFW_BLUE_BITS, primary.getValue().get(0).blueBits());
                glfwWindowHint(GLFW_REFRESH_RATE, primary.getValue().get(0).refreshRate());
                win = glfwCreateWindow(primary.getValue().get(0).width(), primary.getValue().get(0).height(), app.TITLE, primary.getKey(), 0);
            }
            else{
                LOGGER.log(Level.WARNING, "primary monitor not found");
            }
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
