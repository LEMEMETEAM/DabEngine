package DabEngine.Input;

import DabEngine.Observer.Event;

public class MouseEvent implements Event {
	
	private int button;
	private int action;
	private int modifier;
	
	public MouseEvent(int button, int action, int mod) {
		this.button = button;
		this.action = action;
		this.modifier = mod;
	}
	
	public int getButton() {
		return button;
	}
	
	public int getModifier() {
		return modifier;
	}
	
	public int getAction() {
		return action;
	}
}
