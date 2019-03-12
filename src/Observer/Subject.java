package Observer;

import java.util.ArrayList;

import Input.KeyEvent;
import Input.MouseEvent;

import static org.lwjgl.glfw.GLFW.*;

public abstract class Subject {
	
	private ArrayList<Observer> observers = new ArrayList<>();
	
	public void addObserver(Observer o) {
		observers.add(o);
	}
	public void removeObserver(Observer o) {
		observers.remove(o);
	}
	public void dispatchKeyEvent(KeyEvent e) {
		for(int i = 0; i < observers.size(); i++) {
			Observer o = observers.get(i);
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
			Observer o = observers.get(i);
			switch(e.getAction()) {
				case GLFW_PRESS:
					o.onMousePress(e);
				case GLFW_RELEASE:
					o.onMouseRelease(e);
			}
		}
	}

}
