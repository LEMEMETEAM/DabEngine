package Entities.Components;

import Input.InputHandler;

public abstract class Action extends Component {
	
	public abstract void update(InputHandler handler);
}
