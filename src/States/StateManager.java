package States;

public class StateManager {
	
	private static State state;
	
	public static State getState() {
		return state;
	}
	
	public static void setState(State state) {
		StateManager.state = state;
	}
}
