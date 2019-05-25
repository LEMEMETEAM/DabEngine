package DabEngine.System;

import java.util.HashSet;

public class EventManager {
	
	private static HashSet<Event> event_stack = new HashSet<>();
	
	public static void submitEvent(Event e) {
		event_stack.add(e);
	}
	
	public static <T> T receiveEvent(Class<T> clz) {
		for(Event e : event_stack) {
			if(clz.isInstance(e)) {
				T cast = clz.cast(e);
				event_stack.remove(e);
				return cast;
			}
		}
		return null;
	}
	
	public static <T> boolean hasEvent(Class<T> clz) {
		for(Event e  : event_stack) {
			if(clz.isInstance(e)) {
				return true;
			}
		}
		return false;
	}
}
