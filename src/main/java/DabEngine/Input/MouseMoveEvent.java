package DabEngine.Input;

import org.joml.Vector2d;

import DabEngine.Observer.Event;

public class MouseMoveEvent implements Event
{

    private Vector2d pos;
    private Vector2d delta;

    public MouseMoveEvent(Vector2d pos, Vector2d delta)
    {
        this.pos = pos;
        this.delta = delta;
    }

    public Vector2d getMousePos()
    {
        return pos;
    }

    public Vector2d getMouseDelta()
    {
        return delta;
    }
}