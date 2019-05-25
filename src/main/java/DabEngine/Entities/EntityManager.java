package DabEngine.Entities;

import java.util.ArrayList;
import java.util.WeakHashMap;

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
}
