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
        long lastTime = System.nanoTime();
        double ns = 1000000000.0 / TARGET_FPS;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        double delta = 0.0;
        
		while (!glfwWindowShouldClose(mainWindow.getWin())) {
			if(EventManager.INSTANCE.hasEvent(ResizeEvent.class)){
                //just remove it
                EventManager.INSTANCE.receiveEvent(ResizeEvent.class);
	            glViewport(0, 0, mainWindow.getWidth(), mainWindow.getHeight());
	        }
            long now = System.nanoTime();
            delta += (now - lastTime);
            lastTime = now;
            while (delta >= ns) {
                app.update();
                glfwPollEvents();
                updates++;
                delta-=ns;
                Timer.update();
            }
            app.render();
            glfwSwapBuffers(mainWindow.getWin());
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                FRAMES = frames;
                UPDATES = updates;
                System.out.println(updates + " ups, " + frames + " fps");
                updates = 0;
                frames = 0;
            }
        }
        end();
    }

    public void init(App app) {
        
        mainWindow = new Window(app.WIDTH, app.HEIGHT, app.TITLE, app.hints, app.fullscreen);
        
        if(mainWindow.isLoaded()) {
        
            glViewport(0, 0, mainWindow.getWidth(), mainWindow.getHeight());
            glScissor(0, 0, mainWindow.getWidth(), mainWindow.getHeight());
            
		    glfwSetKeyCallback(mainWindow.getWin(), InputHandler.INSTANCE.new Keyboard());
		    glfwSetCursorPosCallback(mainWindow.getWin(), InputHandler.INSTANCE.new MousePos());
		    glfwSetMouseButtonCallback(mainWindow.getWin(), InputHandler.INSTANCE.new MouseButton());
		
		    glEnable(GL_BLEND);
		    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		    glEnable(GL_STENCIL_TEST);
		    
		    this.app = app;
		    
		    app.init();
        }
        else {
        	LOGGER.log(Level.SEVERE, "Window not loaded");
        }
    }
    
    public Graphics createGraphics() {
    	if(!graphics_init) {
    		graphics_init = true;
    		return new Graphics();
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