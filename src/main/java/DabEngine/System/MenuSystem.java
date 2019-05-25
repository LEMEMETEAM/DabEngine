package DabEngine.System;

import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CMenu;

public class MenuSystem extends ComponentSystem {
	
	public void update() {
		for(Entity e : EntityManager.entitiesWithComponents(CMenu.class)) {
			CMenu m = e.getComponent(CMenu.class);
			m.menu.update();
		}
	}
	
	public void render(Graphics g) {
		for(Entity e : EntityManager.entitiesWithComponents(CMenu.class)) {
			CMenu m = e.getComponent(CMenu.class);
			m.menu.render(g);
		}
	}

}
