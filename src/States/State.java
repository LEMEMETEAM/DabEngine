package States;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import Input.InputHandler;
import System.System;

public abstract class State {
	private ArrayList<WeakReference<System>> sys = new ArrayList<>();
	public void render() {
		for(WeakReference<System> system : sys) {
			system.get().render();
		}
	}
	public void tick() {
		for(WeakReference<System> system : sys) {
			system.get().update();
		}
	}
	public abstract void processInput(InputHandler handler);
	public void addSystem(System system) {
		sys.add(new WeakReference<>(system));
		system.addedToState(this);
	}
	public <T> T getSystem(Class<T> clazz) {
		for(WeakReference<System> system : sys) {
			if(clazz.isAssignableFrom(system.get().getClass())) {
				return clazz.cast(system.get());
			}
		}
		return null;
	}
	public abstract void init();
}
