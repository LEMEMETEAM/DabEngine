package DabEngine.Core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.opengl.GLDebugMessageCallback;
import org.lwjgl.system.Configuration;

import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.OpenGL.Window;
import DabEngine.Input.InputHandler;
import DabEngine.Utils.Timer;

/**
 * Main Engine class
 */
public class Engine {
	
    private Window mainWindow = null;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private App app;
    public static double TIMESCALE = 1;
    public static double TARGET_FPS = 60 * TIMESCALE;
    private boolean graphics_init;
    public int FRAMES, UPDATES;
    private final StringBuilder stats_builder = new StringBuilder();

    /**
     * Destroys the window and terminates the program
     */
    public void end() {
    	glfwDestroyWindow(mainWindow.getWin());
        glfwTerminate();
        app.dispose();
        System.exit(0);
    }

    /**
     * The main game loop/
     * Updates timers and uses the delta value from {@link Timer} to update the {@link App} update and render methods.
     * Also prints an FPS counter to console.
     * The loopends when the window is closed
     */
    public void run() {
        double ns = 1.0 / TARGET_FPS;
        int updates = 0;
        int frames = 0;
        double acc = 0.0;
        double timer = Timer.getTime();
        
		while (!glfwWindowShouldClose(mainWindow.getWin())) {

            //update
            Timer.update();
            acc += Timer.getDelta();
            while (acc >= ns) {
                app.update();
                glfwPollEvents();
                updates++;
                acc-=ns;
            }

            //render
            app.render();
            glfwSwapBuffers(mainWindow.getWin());
            frames++;

            if (Timer.getTime() - timer > 1.0) {
                timer ++;
                FRAMES = frames;
                UPDATES = updates;
                LOGGER.log(Level.INFO, stats_builder.append(updates).append(" ups, ").append(frames).append(" fps").append("\n").append(1.0f/frames).append(" seconds per frame").toString());
                stats_builder.setLength(0);
                updates = 0;
                frames = 0;
            }
        }
        end();
    }

    /**
     * Main init method that initializes the window, opengl and the app.
     * @param app the app to init
     */
    public void init(App app) {
        
        mainWindow = new Window(app);
        
        if(mainWindow.isLoaded()) {
            
		    glfwSetKeyCallback(mainWindow.getWin(), InputHandler.INSTANCE.new Keyboard());
		    glfwSetCursorPosCallback(mainWindow.getWin(), InputHandler.INSTANCE.new MousePos());
		    glfwSetMouseButtonCallback(mainWindow.getWin(), InputHandler.INSTANCE.new MouseButton());
        
            glEnable(GL_DEBUG_OUTPUT);
    
            glDebugMessageCallback(new GLDebugMessageCallback(){
                @Override
                public void invoke(int arg0, int arg1, int arg2, int arg3, int arg4, long arg5, long arg6) {
                    System.out.println(getMessage(arg4, arg5));
                }
            }, 0);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            //glEnable(GL_SCISSOR_TEST);
            //glScissor(0, 0, mainWindow.getWidth(), mainWindow.getHeight());
		    
		    this.app = app;
		    
            app.init();
            
            Timer.init();
        }
        else {
        	LOGGER.log(Level.SEVERE, "Window not loaded");
        }
    }
    
    /**
     * creates the {@link Graphics} object. Can only be done once.
     * @param app app which will be passed to Graphics
     */
    public Graphics createGraphics(App app) {
    	if(!graphics_init) {
    		graphics_init = true;
    		return new Graphics(app);
    	}
    	else {
    		LOGGER.log(Level.SEVERE, "GRAPHICS ALREADY CREATED!");
    		return null;
    	}
    }

    public Window getMainWindow() {
        return mainWindow;
    }
}