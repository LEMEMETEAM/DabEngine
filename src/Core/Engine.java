package Core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import Graphics.Window;
import Input.InputHandler;

public class Engine {
	
    private Window mainWindow = null;
    private InputHandler inputhandler;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private App app;
    
    public void end() {
    	glfwDestroyWindow(mainWindow.getWin());
        glfwTerminate();
        System.exit(0);
    }

    public void run() {

        long lastTime = System.nanoTime();
        double ns = 1000000000.0 / 60.0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        double delta = 0.0;
        
		while (!glfwWindowShouldClose(mainWindow.getWin())) {
			if(mainWindow.isResized()){
	            glViewport(0, 0, mainWindow.getWidth(), mainWindow.getHeight());
	        }
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1.0) {
                app.update();
                glfwPollEvents();
                updates++;
                delta--;
            }
            app.render();
            glfwSwapBuffers(mainWindow.getWin());
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
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
		    inputhandler = new InputHandler();
		
		    glfwSetKeyCallback(mainWindow.getWin(), inputhandler);
		    glfwSetCursorPosCallback(mainWindow.getWin(), inputhandler);
		
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

    public Window getMainWindow() {
        return mainWindow;
    }
}