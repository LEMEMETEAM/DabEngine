package DabEngine.Observer;

import java.util.HashSet;

import DabEngine.Observer.Event;

public enum EventManager {

	INSTANCE;
	
	private HashSet<Event> event_stack = new HashSet<>();
	
	public void submitEvent(Event e) {
		event_stack.add(e);
	}
	
	public <T> T receiveEvent(Class<T> clz) {
		for(Event e : event_stack) {
			if(clz.isInstance(e)) {
				T cast = clz.cast(e);
				event_stack.remove(e);
				return cast;
			}
		}
		return null;
	}
	
	public <T> boolean hasEvent(Class<T> clz) {
		for(Event e  : event_stack) {
			if(clz.isInstance(e)) {
				return true;
			}
		}
		return false;
	}
}
