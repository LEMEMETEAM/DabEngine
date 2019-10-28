package DabEngine.Input;

import DabEngine.Observer.Event;

public class KeyEvent implements Event {
	
	private int key_id;
	private int action;
	private int scancode;
	private int modifier;
	
	public KeyEvent(int key, int scancode, int action, int mods) {
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
}
