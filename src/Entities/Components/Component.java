package Entities.Components;

import java.io.Serializable;

import Entities.Entity;

public abstract class Component implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1910311515690798240L;
	public Entity entity;
	public void addedToGameObject(Entity e) {
		entity = e;
	}
}
