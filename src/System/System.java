package System;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import Entities.GameObject;
import States.State;

public abstract class System {
	
	public WeakReference<State> state;
	protected ArrayList<WeakReference<GameObject>> obj = new ArrayList<>();
	protected ArrayList<Class<?>> types = new ArrayList<>();
	
	public void addGameObject(GameObject g) {
		obj.add(new WeakReference<>(g));
		g.addedToSystem(this);
	}
	
	public void addedToState(State state) {
		this.state = new WeakReference<>(state);
	}
	
	public abstract void update();
	
	public abstract void render();
}
