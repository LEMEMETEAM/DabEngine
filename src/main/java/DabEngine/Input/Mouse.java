package DabEngine.Input;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2d;

import DabEngine.Observer.IEventSender;

public class Mouse extends IEventSender<MouseEventListener>
{

    private double lastX, lastY;

    public void onMouseButtonDown(int button, int mod)
    {
        MouseEvent e = new MouseEvent(button, GLFW_PRESS, mod);

        for(MouseEventListener l : observers)
        {
            l.onMouseButtonDown(e);
        }
    }

    public void onMouseButtonUp(int button, int mod)
    {
        MouseEvent e = new MouseEvent(button, GLFW_RELEASE, mod);

        for(MouseEventListener l : observers)
        {
            l.onMouseButtonUp(e);
        }
    }

    public void onMouseMove(double x, double y)
    {
        double deltax = x - lastX;
        double deltay = y - lastY;

        lastX = x;
        lastY = y;

        MouseMoveEvent e = new MouseMoveEvent(new Vector2d(x, y), new Vector2d(deltax, deltay));

        for(MouseEventListener l : observers)
        {
            l.onMouseMove(e);
        }
    }

    public Vector2d getPos()
    {
        return new Vector2d(lastX, lastY);
    }
}