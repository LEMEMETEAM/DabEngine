package Entities;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.joml.*;

import Entities.Components.Component;

import System.System;

public abstract class GameObject {
	
	private ArrayList<WeakReference<Component>> comps = new ArrayList<>();
	public WeakReference<System> system;
	
	public void addComponent(Component c) {
		comps.add(new WeakReference<>(c));
		c.addedToGameObject(this);
	}
	
	public void addedToSystem(System system) {
		this.system = new WeakReference<>(system);
	}
	
	public <T> T getComponent(Class<T> cl) {
		for(WeakReference<Component> c : comps) {
			if(cl.isAssignableFrom(c.get().getClass())) {
				return cl.cast(c.get());
			}
		}
		return null;
	}
	
	public <T> boolean hasComponent(Class<T> cl) {
		for(WeakReference<Component> c : comps) {
			if(c.get().getClass().equals(cl)) {
				return true;
			}
		}
		return false;
	}
}
