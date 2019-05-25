package DabEngine.Input;

import DabEngine.Observer.ActionEventSender;

public class MouseEvent implements Event {
	
	private int button;
	private int action;
	private int modifier;
	private ActionEventSender sub;
	
	public MouseEvent(ActionEventSender sub, int button, int action, int mod) {
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
	
	public ActionEventSender getSubject() {
		return sub;
	}
}
