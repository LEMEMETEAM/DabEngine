package System;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.logging.Logger;

import Entities.GameObject;
import States.Scene;

public abstract class System {
	
	public WeakReference<Scene> state;
	protected ArrayList<WeakReference<GameObject>> obj = new ArrayList<>();
	protected ArrayList<Class<?>> types = new ArrayList<>();
	protected static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public void addGameObject(GameObject g) {
		obj.add(new WeakReference<>(g));
		g.addedToSystem(this);
	}
	
	public void addGameObject(int index, GameObject g) {
		obj.add(index, new WeakReference<>(g));
		g.addedToSystem(this);
	}
	
	public <T> T getGameObject(Class<T> clazz) {
		for(WeakReference<GameObject> obj : obj) {
			if(clazz.isAssignableFrom(obj.get().getClass())) {
				return clazz.cast(obj.get());
			}
		}
		return null;
	}
	
	public void addedToState(Scene state) {
		this.state = new WeakReference<>(state);
	}
	
	public abstract void update();
	
	public abstract void render();
}
