package Input;

import Observer.Event;
import Observer.ActionEventSender;

public class KeyEvent implements Event {
	
	private int key_id;
	private int action;
	private int scancode;
	private int modifier;
	private ActionEventSender subject;
	
	public KeyEvent(ActionEventSender sub, int key, int scancode, int action, int mods) {
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
	
	public ActionEventSender getSubject() {
		return subject;
	}
}
