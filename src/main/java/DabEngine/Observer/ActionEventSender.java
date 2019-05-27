package DabEngine.Observer;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import DabEngine.Input.KeyEvent;
import DabEngine.Input.MouseEvent;

public abstract class ActionEventSender<T extends IEventListener> extends IEventSender<T> {
	
	public void dispatchKeyEvent(KeyEvent e) {
		for(int i = 0; i < observers.size(); i++) {
			ActionEventListener o = (ActionEventListener)observers.get(i);
			switch(e.getAction()) {
				case GLFW_PRESS:
					o.onKeyPress(e);
					break;
				case GLFW_RELEASE:
					o.onKeyRelease(e);
					break;
			}
		}
	}
	
	public void dispatchMouseEvent(MouseEvent e) {
		for(int i = 0; i < observers.size(); i++) {
			ActionEventListener o = (ActionEventListener)observers.get(i);
			switch(e.getAction()) {
				case GLFW_PRESS:
					o.onMousePress(e);
					break;
				case GLFW_RELEASE:
					o.onMouseRelease(e);
					break;
			}
		}
	}

}
