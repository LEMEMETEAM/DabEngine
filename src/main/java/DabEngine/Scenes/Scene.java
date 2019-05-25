package DabEngine.Scenes;

import java.util.HashSet;

import DabEngine.Graphics.Camera;
import DabEngine.System.ComponentSystem;

public abstract class Scene {
	private HashSet<ComponentSystem> sys = new HashSet<>();
	public Camera camera;
	
	public void render(Graphics g) {
		for(ComponentSystem system : sys) {
			system.render(g);
		}
	}
	public void tick() {
		for(ComponentSystem system : sys) {
			system.update();
		}
	}
	public void addSystem(ComponentSystem system) {
		sys.add(system);
		system.addedToScene(this);
	}
	public <T> T getSystem(Class<T> clazz) {
		for(ComponentSystem system : sys) {
			if(clazz.isAssignableFrom(system.getClass())) {
				return clazz.cast(system);
			}
		}
		return null;
	}
	public abstract void init();
}
