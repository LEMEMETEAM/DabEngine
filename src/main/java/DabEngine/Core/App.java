package DabEngine.Core;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWErrorCallback;

import DabEngine.Utils.Timer;

public class App implements IDisposable
{

    private AppConfig config;
    private AppAdapter adapter;
    private Window window;
    private Timer timer;
    private static boolean glfwInit;

    static void initGLFW()
    {
        if(glfwInit) return;
        if(!glfwInit()) throw new RuntimeException("GLFW Not Initialized");
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
        if(!glfwInit) glfwInit = true;
    }

    public App(AppAdapter adapter, AppConfig config)
    {

        if(config.title.isEmpty()) config.title = adapter.getClass().getSimpleName();

        initGLFW();

        this.config = config;
        this.timer = new Timer();
        this.adapter = adapter;

        window = Window.create(config);
        if(window == null) throw new RuntimeException("Window not created");
        window.init(adapter);

        adapter.connectApp(this);

        adapter.init();

        window.showWindow(true);
    }

    public void run()
    {

        double ns = 1.0 / config.targetFPS;
        double acc = 0.0;


        while(!window.shouldClose())
        {

            timer.update();
            acc += timer.getDelta();
            while(acc >= ns)
            {
                adapter.update();
                glfwPollEvents();
                acc-=ns;
            }

            adapter.render();
            glfwSwapBuffers(window.getHandle());

        }

        adapter.dispose();
        window.dispose();
        System.exit(0);

    }

    /**
     * @return the window
     */
    public Window getWindow() {
        return window;
    }

    /**
     * @return the input
     */
    public Input getInput() {
        return window.getInput();
    }

    /**
     * @return the timer
     */
    public Timer getTimer() {
        return timer;
    }

    /**
     * @return the config
     */
    public AppConfig getConfig() {
        return config;
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        adapter.dispose();
        window.dispose();
    }

}
