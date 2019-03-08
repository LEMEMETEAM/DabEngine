package System;

import Entities.Entity;
import Entities.EntityManager;
import Entities.Components.CMenu;

public class MenuSystem extends System {
	
	public void update() {
		for(Entity e : EntityManager.entitiesWithComponents(CMenu.class)) {
			CMenu m = e.getComponent(CMenu.class);
			m.menu.update();
		}
	}
	
	public void render() {
		for(Entity e : EntityManager.entitiesWithComponents(CMenu.class)) {
			CMenu m = e.getComponent(CMenu.class);
			m.menu.render();
		}
	}

}
