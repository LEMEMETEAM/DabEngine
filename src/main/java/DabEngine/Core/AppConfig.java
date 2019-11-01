package DabEngine.Core;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.IntBuffer;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import DabEngine.Utils.Utils;

public class AppConfig {
    public String title = "";

    public int width, height;

    public boolean vSync, resizable = true, decorated = true;

    public float targetFPS = 60;

    public DisplayMode fullscreenMode;

    public static Monitor[] getMonitors() {
        App.initGLFW();
        PointerBuffer ms = glfwGetMonitors();
        Monitor[] monitors = new Monitor[ms.limit()];
        for (int i = 0; i < ms.limit(); i++) {
            monitors[i] = Utils.toMonitorClass(ms.get(i));
        }

        return monitors;
    }

    public static Monitor getPrimaryMonitor() {
        App.initGLFW();
        return Utils.toMonitorClass(glfwGetPrimaryMonitor());
    }

    public static DisplayMode[] getDisplayModes(Monitor monitor)
    {
        App.initGLFW();
        GLFWVidMode.Buffer vms = glfwGetVideoModes(monitor.monitor);
        DisplayMode[] modes = new DisplayMode[vms.limit()];
        for(int i = 0; i < vms.limit(); i++)
        {
            GLFWVidMode vm = vms.get(i);
            modes[i] = new DisplayMode(monitor, vm.width(), vm.height(), vm.refreshRate(), vm.redBits() + vm.greenBits() + vm.blueBits());
        }
        return modes;
    }

    public static DisplayMode getDisplayMode(Monitor monitor)
    {
        App.initGLFW();
        GLFWVidMode vm = glfwGetVideoMode(monitor.monitor);
        return new DisplayMode(monitor, vm.width(), vm.height(), vm.refreshRate(), vm.redBits() + vm.greenBits() + vm.blueBits());
    }
}