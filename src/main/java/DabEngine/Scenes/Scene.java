package DabEngine.Scenes;

import java.util.HashSet;
import java.util.Stack;

import DabEngine.Graphics.Camera;
import DabEngine.Graphics.Graphics;
import DabEngine.System.ComponentSystem;

public abstract class Scene {
	private HashSet<ComponentSystem> sys = new HashSet<>();
	public Camera camera;
	public Stack<Scene> overlays = new Stack<>();
	
	public void render(Graphics g) {
		for(ComponentSystem system : sys) {
			system.render(g);
		}
		for(Scene s : overlays){
			s.render(g);
		}
	}
	public void tick() {
		for(ComponentSystem system : sys) {
			system.update();
		}
		for(Scene s : overlays){
			s.tick();
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

	public void addOverlay(Scene overlay){
		overlays.push(overlay).init();
	}
	public abstract void init();
}
