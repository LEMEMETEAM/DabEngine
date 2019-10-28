package DabEngine.States;

public class StateManager {
	
	private State state;
	
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		if(this.state != null)
		{
			this.state = state;
		}
	}
}
