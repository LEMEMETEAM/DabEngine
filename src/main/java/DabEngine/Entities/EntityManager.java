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

public enum EntityManager {
	
	INSTANCE;

	private HashMap<Class, ComponentHandle> usedComps = new HashMap<>();
	private int[] entities = new int[32];
	private int next = 1;
	private int highest = 1;
	private ArrayDeque<Integer> recycleBin = new ArrayDeque<>();

	private void resize() {
		int oldsize = entities.length;
		int newsize = oldsize + oldsize / 2;

		entities = Arrays.copyOf(entities, newsize);
	}

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

	public <T extends Component> T component(int entity, Class<T> type){
		ComponentHandle<T> hndl;
		if((hndl = usedComps.get(type)) != null){
			return hndl.component(entity);
		}
		else{
			return null;
		}
	}

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

	public void destroy(int entity){
		recycleBin.add(entity);
		for(ComponentHandle s : usedComps.values()){
			s.comps[entity] = null;
		}
		entities[entity] = 0;
		highest = entities[next];
	}

	public ArrayList<ComponentHandle> handles(Class... types){
		ArrayList<ComponentHandle> temp = new ArrayList<>();
		for(Class c : types){
			temp.add(usedComps.get(c));
		}
		return temp;
	}

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

	public void destroyAll(){
		for(int entity : entities){
			destroy(entity);
		}
	}

	public boolean has(int entity, Class type){
		ComponentHandle hndl;
		if((hndl = usedComps.get(type)) != null){
			return hndl.has(entity);
		}
		return false;
	}

	public static void main(String[] args) {
		float start = System.nanoTime();
		var entt = EntityManager.INSTANCE.create();
		EntityManager.INSTANCE.assign(entt, new CTransform(), CTransform.class);

		var entt2 = EntityManager.INSTANCE.create();
		EntityManager.INSTANCE.assign(entt2, new CText(), CText.class);
		EntityManager.INSTANCE.assign(entt2, new CTransform(), CTransform.class);

		EntityManager.INSTANCE.each((e) -> {
			EntityManager.INSTANCE.component(e, CTransform.class).pos.add(69, 69, 69);
			EntityManager.INSTANCE.component(e, CText.class).text = "lol";
		}, CTransform.class, CText.class);

		CTransform t = EntityManager.INSTANCE.component(entt, CTransform.class);
		CTransform t2 = EntityManager.INSTANCE.component(entt2, CTransform.class);
		CText text = EntityManager.INSTANCE.component(entt2, CText.class);
		float duration = System.nanoTime() - start;
		System.out.println(duration);
		System.out.println(t.pos);
		System.out.println(t2.pos);
		System.out.println(text.text);
	}
}
