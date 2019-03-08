package Entities;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;

import Entities.Components.Component;

public class EntityManager {
	
	public static WeakHashMap<Integer, Entity> entities = new WeakHashMap<>();
	private static int currentID = 0;
	
	public static Entity createEntity(Component... components) {
		int IDToUse = 0;
		if(entities.isEmpty()) {
			IDToUse = nextID();
		}
		else {
			for(Entry<Integer, Entity> e : entities.entrySet()) {
				if(e.getKey() == 0 || e.getValue() == null) {
					IDToUse = e.getKey();
				}
				else {
					IDToUse = nextID();
				}
				break;
			}
		}
		int ID = IDToUse;
		Entity e = new Entity() {{
			entityID = ID;
			for(Component comp : components) {
				addComponent(comp);
			}
		}};
		entities.put(IDToUse, e);
		return e;
	}
	
	public static Entity createEntity() {
		int IDToUse = 0;
		if(entities.isEmpty()) {
			IDToUse = nextID();
		}
		else {
			for(Entry<Integer, Entity> e : entities.entrySet()) {
				if(e.getKey() == 0 || e.getValue() == null) {
					IDToUse = e.getKey();
				}
				else {
					IDToUse = nextID();
				}
				break;
			}
		}
		int ID = IDToUse;
		Entity e = new Entity() {{;
			entityID = ID;
		}};
		entities.put(IDToUse, e);
		return e;
	}
	
	public static void deleteEntity(int id) {
		entities.get(id).removeAllComponents();
		entities.put(id, null);
	}
	
	private static int nextID() {
		currentID += 1;
		return currentID;
	}
	
	public static ArrayList<Entity> entitiesWithComponents(Class<?>... comps) {
		ArrayList<Entity> entitiesWithComponents = new ArrayList<>();
		outerloop:for(Entity e : entities.values()) {
			for(Class<?> cl : comps) {
				if(e == null || !e.hasComponent(cl)) {
					continue outerloop;
				}
				else {
					continue;
				}
			}
			entitiesWithComponents.add(e);
		}
		return entitiesWithComponents;
	}
}
