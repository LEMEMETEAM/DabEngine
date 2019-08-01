package DabEngine.Entities;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.function.*;
import java.util.stream.Collectors;

import DabEngine.Entities.Components.CText;
import DabEngine.Entities.Components.CTransform;
import DabEngine.Entities.Components.Component;
import DabEngine.Entities.Components.ComponentHandle;
import DabEngine.Observer.EventManager;

/**
 * Main EntityManager class.
 * Maps entities to components and checks if a component type has been used before.
 * @see ComponentHandle 
 */
public enum EntityManager {
	
	INSTANCE;

	private HashMap<Class, ComponentHandle> usedComps = new HashMap<>();
	private int[] entities = new int[32];
	private int next = 1;
	private int highest = 1;
	private ArrayDeque<Integer> recycleBin = new ArrayDeque<>();

	/**
	 * resizes  the entities array so that it can accomodate more entities.
	 */
	private void resize() {
		int oldsize = entities.length;
		int newsize = oldsize + oldsize / 2;

		entities = Arrays.copyOf(entities, newsize);
	}

	/**
	 * assigns a component to entity.
	 * Checks to see if the component handle for the component has been used before and uses that component handle, else it creates a new one and adds it to usedComps.
	 * @param entity entity id
	 * @param comp the component to add
	 * @param type the component class
	 */
	public <T extends Component> void assign(int entity, T comp, Class<T> type){
		ComponentHandle<T> hndl;
		if((hndl = usedComps.get(type)) != null){
			hndl.assign(entity, comp);
		}
		else{
			hndl = new ComponentHandle<>(type);
			hndl.assign(entity, comp);
			usedComps.put(type, hndl);
		}
	}

	/**
	 * Gets a component of specified type for the specified entity.
	 * @param <T> type
	 * @param entity entity id
	 * @param type component class
	 * @return the component for that entity
	 */
	public <T extends Component> T component(int entity, Class<T> type){
		ComponentHandle<T> hndl;
		if((hndl = usedComps.get(type)) != null){
			return hndl.component(entity);
		}
		else{
			return null;
		}
	}

	/**
	 * creates an entity, either using thenext available entity id or reusing dead ones.
	 * @return the new entity id
	 */
	public int create(){
		int id;
		if(highest == 1){
			id = next;
			highest++;
		}
		else{
			if(!recycleBin.isEmpty()){
				id = recycleBin.remove();
			}
			else{
				id = next;
				highest++;
			}
		}
		if(next + 1 == entities.length){
			resize();
		}
		entities[id] = id;
		next++;
		//Arrays.sort(entities, 0, highest);
		return id;
	}

	/**
	 * destroys an entity
	 * @param entity entity id
	 */
	public void destroy(int entity){
		recycleBin.add(entity);
		for(ComponentHandle s : usedComps.values()){
			s.comps[entity] = null;
		}
		entities[entity] = 0;
		highest = entities[next];
	}

	/**
	 * Gets the component handles for the specified component types
	 * @param types component classes
	 * @return an arraylist that contains the component handles
	 */
	public ArrayList<ComponentHandle> handles(Class... types){
		ArrayList<ComponentHandle> temp = new ArrayList<>();
		for(Class c : types){
			temp.add(usedComps.get(c));
		}
		return temp;
	}

	/**
	 * Iterates over entities with specified component types and does something to them.
	 * @param itr action to do to entities
	 * @param types component types
	 */
	public void each(EntityIterator itr, Class... types){
		ArrayList<ComponentHandle> handles = handles(types);
		ArrayList<Integer> ids = new ArrayList<>(Arrays.stream(entities).boxed().collect(Collectors.toList()));
		for(ComponentHandle h : handles){
			for(Iterator<Integer> it = ids.iterator(); it.hasNext(); ){
				if(h.comps[it.next()] == null){
					it.remove();
				}
			}
		}
		for(int id : ids){
			itr.each(id);
		}
	}

	/**
	 * destroy all entities
	 */
	public void destroyAll(){
		for(int entity : entities){
			destroy(entity);
		}
	}

	/**
	 * check if entity has component of specified type
	 * @param entity entity id
	 * @param type component class
	 * @return
	 */
	public boolean has(int entity, Class type){
		ComponentHandle hndl;
		if((hndl = usedComps.get(type)) != null){
			return hndl.has(entity);
		}
		return false;
	}
}
