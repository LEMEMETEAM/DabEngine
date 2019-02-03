package Input;

import Observer.Event;
import Observer.Subject;

public class MouseEvent implements Event {
	
	private int button;
	private int action;
	private int modifier;
	private Subject sub;
	
	public MouseEvent(Subject sub, int button, int action, int mod) {
		this.sub = sub;
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
	
	public Subject getSubject() {
		return sub;
	}
}
