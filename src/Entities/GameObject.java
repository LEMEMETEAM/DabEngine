package Entities;

import java.util.ArrayList;

import org.joml.*;

import Entities.Components.Component;

public abstract class GameObject {
	
	private ArrayList<Component> comps = new ArrayList<>();
	
	public void addComponent(Component c) {
		comps.add(c);
		c.addedToGameObject(this);
	}
	
	public <T> T getComponent(Class<T> cl) {
		for(Component c : comps) {
			if(cl.isAssignableFrom(c.getClass())) {
				return cl.cast(c);
			}
		}
		return null;
	}
	
	public <T> boolean hasComponent(Class<T> cl) {
		for(Component c : comps) {
			if(c.getClass().equals(cl)) {
				return true;
			}
		}
		return false;
	}
}
