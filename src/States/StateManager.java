package States;

import Core.Engine;
import Graphics.Camera;
import Graphics.SpriteBatch;
import Input.InputHandler;

public abstract class StateManager {
	
	private static State currentState = null;
	
	public static State getCurrentState() {
		return currentState;
	}
	
	public static void setCurrentState(State newcurrentState) {
		setCurrentState(newcurrentState, null);
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
	
	/*
	 * camera is optional
	 */
	public void render(Engine engine, SpriteBatch batch, Camera cam) {
		currentState.render(engine, batch, cam);
	}
	
	public void update(Engine engine, Camera cam) {
		currentState.update(engine, cam);
	}
	
	public void processInput(Engine engine, InputHandler handler, Camera cam) {
		currentState.processInput(engine, handler, cam);
	}
}
