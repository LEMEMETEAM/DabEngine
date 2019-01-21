package Entities.Components;

import java.util.ArrayList;

public class ComponentSystem {
	protected static final ArrayList<Component> components = new ArrayList<>();
	
	public void add(Component c) {
		if(!components.contains(c))
			components.add(c);
	}
	
	public void remove(Component c) {
		components.remove(c);
	}
	
	public void update() {
		for(int c = 0; c < components.size(); c++) {
			components.get(c).update();
		}
	}
}
