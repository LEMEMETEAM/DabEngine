package DabEngine.Core;

import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import DabEngine.Utils.Utils;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWFramebufferSizeCallback;

public class Window implements IDisposable {

    private AppConfig config;
    private long windowHandle;
    private Input input;
    private int backBufferWidth, backBufferHeight;
    private int logicalWidth, logicalHeight;

    private Window(long windowHandle, AppConfig config){this.config = config; this.windowHandle = windowHandle; this.input = new Input(this);}
    public static Window create(AppConfig config)
    {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, 0);
        glfwWindowHint(GLFW_RESIZABLE, config.resizable ? 1 : 0);

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint (GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        if(Utils.isMac())
            glfwWindowHint (GLFW_OPENGL_FORWARD_COMPAT, 1);

        //create windowed window
        long window = 0;
        if(config.fullscreenMode != null)
        {
            glfwWindowHint(GLFW_REFRESH_RATE, config.fullscreenMode.refreshRate);
            window = glfwCreateWindow(config.fullscreenMode.width, config.fullscreenMode.height, config.title, config.fullscreenMode.monitor.monitor, 0);
        }
        else
        {
            glfwWindowHint(GLFW_DECORATED,  config.decorated ? 1 : 0);
            window = glfwCreateWindow(config.width, config.height, config.title, 0, 0);
        }
        if(window == 0) return null;

        glfwMakeContextCurrent(window);
        glfwSwapInterval(config.vSync ? 1 : 0);
        GL.createCapabilities();


        return new Window(window, config);
    }

    public void init(AppAdapter adapter)
    {
        updateFramebufferInfo();
        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            updateFramebufferInfo();
            adapter.resize(width, height);
            adapter.render();
            glfwSwapBuffers(window);
        });
    }

    private void updateFramebufferInfo()
    {
        try(MemoryStack stack = MemoryStack.stackPush())
        {
            IntBuffer b1 = stack.mallocInt(1);
            IntBuffer b2 = stack.mallocInt(1);

            glfwGetFramebufferSize(windowHandle, b1, b2);
            backBufferWidth = b1.get(0);
            backBufferHeight = b2.get(0);
            glfwGetWindowSize(windowHandle, b1, b2);
            logicalWidth = b1.get(0);
            logicalHeight = b2.get(0);

            config.width = logicalWidth;
            config.height = logicalHeight;
        }
    }

    public long getHandle()
    {
        return windowHandle;
    }

    /**
     * @return the input
     */
    public Input getInput() {
        return input;
    }

    public void setTitle(String title)
    {
        glfwSetWindowTitle(windowHandle, title);
        config.title = title;
    }

    public int getWidth(boolean openGL)
    {
        if(openGL) return backBufferWidth;
        else return logicalWidth;
    }

    public int getHeight(boolean openGL)
    {
        if(openGL) return backBufferHeight;
        else return logicalHeight;
    }

    public Monitor getMonitor()
    {
        Monitor[] ms = AppConfig.getMonitors();
        Monitor result = ms[0];

        try(MemoryStack stack = MemoryStack.stackPush())
        {
            IntBuffer b1 = stack.mallocInt(1);
            IntBuffer b2 = stack.mallocInt(1);

            glfwGetWindowPos(windowHandle, b1, b2);
            int windowX = b1.get(0);
            int windowY = b2.get(0);
            glfwGetWindowSize(windowHandle, b1, b2);
            int windowWidth = b1.get(0);
            int windowHeight = b2.get(0);

            int overlap;
            int bestOverlap = 0;

            for (Monitor monitor : ms) {
                DisplayMode mode = AppConfig.getDisplayMode(monitor);

                overlap = Math.max(0,
                        Math.min(windowX + windowWidth, monitor.virtualX + mode.width)
                                - Math.max(windowX, monitor.virtualX))
                        * Math.max(0, Math.min(windowY + windowHeight, monitor.virtualY + mode.height)
                                - Math.max(windowY, monitor.virtualY));

                if (bestOverlap < overlap) {
                    bestOverlap = overlap;
                    result = monitor;
                }
            }
        }

        return result;
    }

    public void setWindowedMode(int width, int height)
    {
        if(isFullscreen())
        {
            glfwSetWindowMonitor(windowHandle, 0, 0, 0, width, height, 0);
        }
        else
        {
            glfwSetWindowSize(windowHandle, width, height);
        }
        updateFramebufferInfo();
    }

    public void setFullscreen(DisplayMode mode)
    {
        if(isFullscreen())
        {
            DisplayMode current = AppConfig.getDisplayMode(getMonitor());
            if(current.monitor == mode.monitor && current.refreshRate == mode.refreshRate)
            {
                glfwSetWindowSize(windowHandle, mode.width, mode.height);
            }
            else
            {
                glfwSetWindowMonitor(windowHandle, mode.monitor.monitor, 0, 0, mode.width, mode.height, mode.refreshRate);
            }
        }
        else
        {
            glfwSetWindowMonitor(windowHandle, mode.monitor.monitor, 0, 0, mode.width, mode.height, mode.refreshRate);
        }
        updateFramebufferInfo();
    }

    public boolean isFullscreen()
    {
        return glfwGetWindowMonitor(windowHandle) != 0;
    }

    public boolean shouldClose()
    {
        return glfwWindowShouldClose(windowHandle);
    }

    public void showWindow(boolean b)
    {
        if(b) glfwShowWindow(windowHandle);
        else glfwHideWindow(windowHandle);
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }
}
