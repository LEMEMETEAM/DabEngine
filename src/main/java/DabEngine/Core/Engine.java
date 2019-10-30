package DabEngine.Core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GLDebugMessageCallback;

import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.OpenGL.Window;
import DabEngine.Input.Keyboard;
import DabEngine.Input.Mouse;
import DabEngine.Utils.Timer;

/**
 * Main Engine class
 */
public class Engine {
	
    private Window window;
    private App app;
    private Keyboard keyboard;
    private Mouse mouse;
    private Timer timer;

    public static double TIMESCALE = 1;
    public static double TARGET_FPS = 60;
    public int FRAMES, UPDATES;

    private static final Logger LOGGER = Logger.getGlobal();

    public Engine()
    {
        TIMESCALE = 1;
        TARGET_FPS = 60;
        FRAMES = 0;
        UPDATES = 0;

        keyboard = new Keyboard();
        mouse = new Mouse();

        //timer
        timer = new Timer();

        //app
        app = null;

        //window
        window = null;
    }

    /**
     * Destroys the window and terminates the program
     */
    public void end() {
    	glfwDestroyWindow(window.getWin());
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
        double ns = 1.0 / (TARGET_FPS * TIMESCALE);
        int updates = 0;
        int frames = 0;
        double acc = 0.0;
        double time = timer.getTime();
        
		while (!glfwWindowShouldClose(window.getWin())) {

            //update
            timer.update();
            acc += timer.getDelta();
            while (acc >= ns) {
                app.update();
                glfwPollEvents();
                updates++;
                acc-=ns;
            }

            //render
            app.render();
            glfwSwapBuffers(window.getWin());
            frames++;

            if (timer.getTime() - time > 1.0) {
                time++;
                FRAMES = frames;
                UPDATES = updates;
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
    public void init(App app) 
    {
        
        window = new Window(app);
        
        if(window.isLoaded()) 
        {
            
		    glfwSetKeyCallback(window.getWin(), GLFWKeyCallback.create((window, key, scancode, action, mods) -> {
                switch(action){
                    case 0:
                        keyboard.onKeyUp(key, scancode, mods);
                        break;
                    case 1:
                        keyboard.onKeyClicked(key, scancode, mods);
                        break;
                    case 2:
                        keyboard.onKeyDown(key, scancode, mods);
                        break;
                }
            }));
            glfwSetMouseButtonCallback(window.getWin(), GLFWMouseButtonCallback.create((window, button, action, mods) -> {
                switch(action){
                    case 0:
                        mouse.onMouseButtonUp(button, mods);
                        break;
                    case 1:
                        mouse.onMouseButtonDown(button, mods);
                        break;
                }
            }));
            glfwSetCursorPosCallback(window.getWin(), GLFWCursorPosCallback.create((window, x, y) -> {
                mouse.onMouseMove(x, y);
            }));
        
            glEnable(GL_DEBUG_OUTPUT);   
            glDebugMessageCallback(new GLDebugMessageCallback()
            {
                @Override
                public void invoke(int arg0, int arg1, int arg2, int arg3, int arg4, long arg5, long arg6) 
                {
                    Thread.currentThread().dumpStack();
                    System.out.println(getMessage(arg4, arg5));
                }
            }, 0);

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		    
            this.app = app; 
            app.init();

            return;
        }
        
        logger().warning("Window not made");
    }
    
    /**
     * creates the {@link Graphics} object. Can only be done once.
     * @param app app which will be passed to Graphics
     */
    public Graphics createGraphics() 
    {
        return new Graphics(app);
    }

    public Window getWindow() 
    {
        return window;
    }

    /**
     * @return the mouse
     */
    public Mouse getMouse() {
        return mouse;
    }

    /**
     * @return the keyboard
     */
    public Keyboard getKeyboard() {
        return keyboard;
    }

    /**
     * @return the app
     */
    public App getApp() {
        return app;
    }

    public static Logger logger()
    {
        return LOGGER;
    }
}