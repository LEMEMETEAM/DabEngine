package Observer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import Input.KeyEvent;
import Input.MouseEvent;

import static org.lwjgl.glfw.GLFW.*;

public abstract class ActionEventSender {
	
	public ArrayList<WeakReference<ActionEventListener>> observers = new ArrayList<>();
	
	public void addObserver(ActionEventListener o) {
		observers.add(new WeakReference<ActionEventListener>(o));
	}
	
	/*DO NOT USE*/
	@SuppressWarnings("unlikely-arg-type")
	public void removeObserver(ActionEventListener o) {
		observers.remove(o);
	}
	
	public void clearObservers() {
		observers.clear();
	}
	
	public void dispatchKeyEvent(KeyEvent e) {
		for(int i = 0; i < observers.size(); i++) {
			ActionEventListener o = observers.get(i).get();
			switch(e.getAction()) {
				case GLFW_PRESS:
					o.onKeyPress(e);
				case GLFW_RELEASE:
					o.onKeyRelease(e);
			}
		}
	}
	
	public void dispatchMouseEvent(MouseEvent e) {
		for(int i = 0; i < observers.size(); i++) {
			ActionEventListener o = observers.get(i).get();
			switch(e.getAction()) {
				case GLFW_PRESS:
					o.onMousePress(e);
				case GLFW_RELEASE:
					o.onMouseRelease(e);
			}
		}
	}

}
