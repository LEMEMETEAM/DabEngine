package States;

import java.util.ArrayList;

import Core.Engine;
import Graphics.Camera;
import Graphics.Batch.SpriteBatch;
import Input.InputHandler;

public abstract class StateManager {
	
	private static State currentState = null;
	public static final ArrayList<State> states = new ArrayList<>();
	
	public static State getCurrentState() {
		return currentState;
	}
	
	public static void addState(State state) {
		if(!states.contains(state)) {
			states.add(state);
		}
	}
	
	public static <T> T getState(Class<T> clazz) {
		for(State state : states) {
			if(clazz.isAssignableFrom(state.getClass())) {
				return clazz.cast(state);
			}
		}
		return null;
	}
	
	public static void setCurrentState(State newcurrentState) {
		setCurrentState(newcurrentState, null);
	}
	
	public static void initStates() {
		for(State state : states) {
			state.init();
		}
	}
	
	public static void setCurrentState(State newcurrentstate, Transition transition) {
		if(transition != null) {
			transition.in();
			currentState = newcurrentstate;
			transition.out();
		} else {
			currentState = newcurrentstate;
		}
	}
}
