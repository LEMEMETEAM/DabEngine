package Entities;

import java.util.HashSet;
import java.util.Iterator;

import Entities.Components.Component;

public class Entity {
	
	public HashSet<Component> comps = new HashSet<>();
	public int entityID;
	
	public void addComponent(Component c) {
		comps.add(c);
		c.addedToGameObject(this);
	}
	
	public <T> void removeComponent(Class<T> cl) {
		for(Iterator<Component> it = comps.iterator(); it.hasNext(); ) {
			Component c = it.next();
			if(cl.isAssignableFrom(c.getClass())) {
				it.remove();
			}
		}
	}
	
	public void removeAllComponents() {
		comps.clear();
	}
	
	public <T> T getComponent(Class<T> cl) {
		for(Component c : comps) {
			if(cl.isInstance(c)) {
				return cl.cast(c);
			}
		}
		return null;
	}
	
	public <T> boolean hasComponent(Class<T> cl) {
		for(Component c : comps) {
			if(cl.isInstance(c)) {
				return true;
			}
		}
		return false;
	}
}
