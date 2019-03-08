package System;

import java.util.ArrayList;

public class SystemSubject {
private ArrayList<SystemObserver> observers = new ArrayList<>();
	
	public void addObserver(SystemObserver o) {
		observers.add(o);
	}
	public void removeObserver(SystemObserver o) {
		observers.remove(o);
	}
}
