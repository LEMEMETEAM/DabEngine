package DabEngine.Entities;

import java.util.ArrayList;
import java.util.WeakHashMap;

import java.util.function.*;

import DabEngine.Entities.Components.Component;

public class EntityManager {
	
	public static WeakHashMap<Integer, Entity> entities = new WeakHashMap<>();
	private static volatile int currentID = 0;
	
	public static Entity createEntity(Component... components) {
		int IDToUse = 0;
		boolean found = false;
		if(entities.isEmpty()) {
			IDToUse = nextID();
		}
		else {
			for(int i = 1; i <= currentID; i++) {
				if(!entities.containsKey(i) || entities.get(i) == null) {
					IDToUse = i;
					found = true;
					break;
				}
			}
			if(!found) {
				IDToUse = nextID();
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
		boolean found = false;
		if(entities.isEmpty()) {
			IDToUse = nextID();
		}
		else {
			for(int i = 1; i <= currentID; i++) {
				if(!entities.containsKey(i) && entities.get(i) == null) {
					IDToUse = i;
					found = true;
					break;
				}
			}
			if(!found) {
				IDToUse = nextID();
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
		entities.remove(id);
		if(currentID == id) {
			currentID -= 1;
		}
		else if(currentID == 0) {
			return;
		}
	}
	
	public static void clearAllEntities() {
		entities.clear();
		currentID = 0;
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

	public interface Sorting{
		boolean sort(Entity e1, Entity e2);
	}

	public static ArrayList<Entity> sort(Sorting sort, Class<?>... cls){
		ArrayList<Entity> e = entitiesWithComponents(cls);
		boolean swaped;
		for(int i = 0; i < e.size()-1; i++){
			swaped = false;
			for(int j = 0; j < e.size()-i-1; j++){
				if(sort.sort(e.get(j), e.get(j+1))){
					Entity temp = e.get(j);
					e.set(j, e.get(j+1));
					e.set(j+1, temp);
					swaped = true;
				}
			}
			if(swaped == false){
				break;
			}
		}
		return e;
	}
}
