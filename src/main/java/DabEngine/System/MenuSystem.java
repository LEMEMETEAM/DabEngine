package DabEngine.System;

import DabEngine.Entities.EntityFilter;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CMenu;
import DabEngine.Graphics.Graphics;

public class MenuSystem extends ComponentSystem {
	
	public void update() {
		EntityManager.INSTANCE.each(e -> {
			CMenu m = EntityManager.INSTANCE.component(e, CMenu.class);
			m.menu.update();
		}, EntityFilter.has(CMenu.class));
	}
	
	public void render(Graphics g) {
		EntityManager.INSTANCE.each(e -> {
			CMenu m = EntityManager.INSTANCE.component(e, CMenu.class);
			m.menu.render(g);
		}, EntityFilter.has(CMenu.class));
	}

}
