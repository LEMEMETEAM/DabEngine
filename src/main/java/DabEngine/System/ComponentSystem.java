package DabEngine.System;

import java.util.logging.Logger;

import DabEngine.Graphics.Graphics;
import DabEngine.Scenes.Scene;

/**
 * Base ComponentSystem class.
 * All other ComponentSystems must extend this.
 */

public abstract class ComponentSystem {
	
	/**
	 * Reference to scene that this ComponentSystem is part of.
	 */
	protected Scene scene;
	protected static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	/**
	 * Setter for {@link scene}
	 */
	public void addedToScene(Scene scene) {
		this.scene = scene;
	}
	
	/**
	 * Called every frame.
	 * Base update method used by ComponentSystems.
	 */
	public abstract void update();
	
	/**
	 * Called every frame.
	 * Base update method used by ComponentSystems.
	 */
	public abstract void render(Graphics g);
}
