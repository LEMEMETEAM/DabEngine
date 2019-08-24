package DabEngine.States;

public class StateManager {
	
	private State state;
	
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		if(this.state != null && !this.state.isInterruptable())
		{
			if(this.state.isFinished())
			{
				state.setFinished(false);
				this.state = state;
			}
		}
		else
		{
			state.setFinished(false);
			this.state = state;
		}
	}
}
