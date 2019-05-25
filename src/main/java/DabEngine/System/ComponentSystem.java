package DabEngine.System;

import java.util.logging.Logger;

import DabEngine.Graphics.Graphics;
import DabEngine.Scenes.Scene;

public abstract class ComponentSystem {
	
	protected Scene scene;
	protected static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public void addedToScene(Scene scene) {
		this.scene = scene;
	}
	
	public abstract void update();
	
	public abstract void render(Graphics g);
}
