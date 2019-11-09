package DabEngine.Core;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import DabEngine.Input.Keyboard;
import DabEngine.Input.Mouse;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class Input implements IDisposable
{

    private Mouse mouse;
    private Keyboard keyboard;

    private final GLFWKeyCallback keyCallback = GLFWKeyCallback.create((Window, keycode, scancode, action, mod) -> {

        switch(action)
        {
            case GLFW_RELEASE:
                keyboard.onKeyUp(keycode, scancode, mod);
                break;
            case GLFW_PRESS:
            case GLFW_REPEAT:
                keyboard.onKeyDown(keycode, scancode, mod);
                break;
        }
    });

    private final GLFWMouseButtonCallback mouseButtonCallback = GLFWMouseButtonCallback.create((window, button, action, mod) -> {
        switch(action)
        {
            case GLFW_RELEASE:
                mouse.onMouseButtonUp(button, mod);
                break;
            case GLFW_PRESS:
                mouse.onMouseButtonDown(button, mod);
        }
    });

    private final GLFWCursorPosCallback mousePosCallback = GLFWCursorPosCallback.create((window, x, y) -> {
        mouse.onMouseMove(x, y);
    });

    public Input(Window window)
    {
        this.mouse = new Mouse();
        this.keyboard = new Keyboard();
        
        glfwSetKeyCallback(window.getHandle(), keyCallback);
        glfwSetMouseButtonCallback(window.getHandle(), mouseButtonCallback);
        glfwSetCursorPosCallback(window.getHandle(), mousePosCallback);
    }

    /**
     * @return the keyboard
     */
    public Keyboard getKeyboard() {
        return keyboard;
    }

    /**
     * @return the mouse
     */
    public Mouse getMouse() {
        return mouse;
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        keyCallback.free();
        mousePosCallback.free();
        mouseButtonCallback.free();
    }
}
