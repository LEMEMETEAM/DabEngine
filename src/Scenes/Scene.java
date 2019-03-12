package Scenes;

import java.util.ArrayList;
import java.util.HashSet;

import Graphics.Graphics;
import Input.InputHandler;
import System.System;

public abstract class Scene {
	private HashSet<System> sys = new HashSet<>();
	public void render(Graphics g) {
		for(System system : sys) {
			system.render(g);
		}
	}
	public void tick() {
		for(System system : sys) {
			system.update();
		}
	}
	public void addSystem(System system) {
		sys.add(system);
		system.addedToScene(this);
	}
	public <T> T getSystem(Class<T> clazz) {
		for(System system : sys) {
			if(clazz.isAssignableFrom(system.getClass())) {
				return clazz.cast(system);
			}
		}
		return null;
	}
	public abstract void init();
}
