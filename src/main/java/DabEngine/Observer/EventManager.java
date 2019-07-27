package DabEngine.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observer;

import DabEngine.Observer.Event;

public enum EventManager {

	INSTANCE;
	
	private HashMap<Class, ArrayList<IEventListener>> listeners = new HashMap<>();

	public void submit(Event e, Class clz){
		for(var o : listeners.get(clz)){
			o.onNotify(e);
		}
	}

	public void subscribe(Class eventType, IEventListener listener){
		listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
	}
}
