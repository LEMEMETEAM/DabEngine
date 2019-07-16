package DabEngine.Core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.OpenGL.ResizeEvent;
import DabEngine.Graphics.OpenGL.Window;
import DabEngine.Input.InputHandler;
import DabEngine.Observer.EventManager;
import DabEngine.Utils.Timer;

public class Engine {
	
    private Window mainWindow = null;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private App app;
    public static double TIMESCALE = 1;
    public static double TARGET_FPS = 60 * TIMESCALE;
    private boolean graphics_init;
    public int FRAMES, UPDATES;
    
    public void end() {
    	glfwDestroyWindow(mainWindow.getWin());
        glfwTerminate();
        System.exit(0);
    }

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
                LOGGER.log(Level.INFO, updates + " ups, " + frames + " fps");
                updates = 0;
                frames = 0;
            }
        }
        end();
    }

    public void init(App app) {
        
        mainWindow = new Window(app);
        
        if(mainWindow.isLoaded()) {
            
		    glfwSetKeyCallback(mainWindow.getWin(), InputHandler.INSTANCE.new Keyboard());
		    glfwSetCursorPosCallback(mainWindow.getWin(), InputHandler.INSTANCE.new MousePos());
		    glfwSetMouseButtonCallback(mainWindow.getWin(), InputHandler.INSTANCE.new MouseButton());
        
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            //glEnable(GL_SCISSOR_TEST);
            //glScissor(0, 0, mainWindow.getWidth(), mainWindow.getHeight());
		    
		    this.app = app;
		    
		    app.init();
        }
        else {
        	LOGGER.log(Level.SEVERE, "Window not loaded");
        }
    }
    
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