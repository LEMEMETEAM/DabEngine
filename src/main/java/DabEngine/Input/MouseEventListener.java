package DabEngine.Input;

import DabEngine.Observer.Event;
import DabEngine.Observer.IEventListener;

public interface MouseEventListener extends IEventListener {

    public abstract void onMouseButtonDown(MouseEvent e);
    public abstract void onMouseButtonUp(MouseEvent e);
    public abstract void onMouseMove(MouseMoveEvent e);

}