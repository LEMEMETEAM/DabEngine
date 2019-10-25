package DabEngine.Entities;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import DabEngine.Entities.Components.Component;
import DabEngine.Entities.Components.ComponentHandle;

/**
 * Main EntityManager class.
 * Maps entities to components and checks if a component type has been used before.
 * @see ComponentHandle 
 */
public enum EntityManager {
	
	INSTANCE;

	private HashMap<Class, ComponentHandle> usedComps = new HashMap<>();
	private boolean[] entities = new boolean[32];
	private int next = 0;
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
		if(!recycleBin.isEmpty()){
			id = recycleBin.remove();
		}
		else{
			id = next;
		}

		if(next + 1 == entities.length){
			resize();
		}
		entities[id] = true;
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
			if(s.comps.length > entity) s.comps[entity] = null;
		}
		entities[entity] = false;
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
	 * @param filter component filter
	 */
	public void each(EntityIterator itr, EntityFilter filter){
		ArrayList<Integer> ids = new ArrayList<>();
		for (int i = 0; i < entities.length; i++) if(entities[i]) ids.add(i);
		for(Iterator<Integer> it = ids.iterator(); it.hasNext(); ){
			if(!(filter.boolFunc.test(it.next()))){
				it.remove();
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
		for(int i = 0; i < entities.length; i++){
			if(entities[i]){
				destroy(i);
			}
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

	/**
	 * alloc a block of entities for pooling
	 */
	public int[] alloc(int size){
		int[] e = new int[size];
		for(int i = 0; i < size; i++){
			e[i] = create();
		}
		return e;
	}

	/**
	 * frees a previously made block of entities
	 */
	public void free(int[] e){
		for(int i = 0; i < e.length; i++){
			destroy(e[i]);
		}
	}

	/**
	 * Clears all components of
	 * entity but doesn't destroy the entity
	 */
	public void clear(int entity){
		for(ComponentHandle s : usedComps.values()){
			if(s.comps.length > entity) s.comps[entity] = null;
		}
	}
}
