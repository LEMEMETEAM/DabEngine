package System;

import java.util.HashSet;

import Observer.Event;

public class EventManager {
	
	private HashSet<Event> event_stack = new HashSet<>();
	
	public void submitEvent(Event e) {
		event_stack.add(e);
	}
	
	public <T> T receiveEvent(Class<T> clz) {
		for(Event e : event_stack) {
			if(clz.isInstance(e)) {
				return clz.cast(e);
			}
		}
		return null;
	}
}
