package Input;

import Observer.Event;
import Observer.Observer;
import Observer.Subject;

public class KeyEvent implements Event {
	
	private int key_id;
	private int action;
	private int scancode;
	private int modifier;
	private Subject subject;
	
	public KeyEvent(Subject sub, int key, int scancode, int action, int mods) {
		subject = sub;
		key_id = key;
		this.scancode = scancode;
		this.action = action;
		modifier = mods;
	}
	
	public int getKey() {
		return key_id;
	}
	
	public int getAction() {
		return action;
	}
	
	public int getModifier() {
		return modifier;
	}
	
	public int getScancode() {
		return scancode;
	}
	
	public Subject getSubject() {
		return subject;
	}
}
