package System;

import Entities.Entity;
import Entities.EntityManager;
import Entities.Components.CAction;
import Graphics.Graphics;

public class ActionSystem extends ComponentSystem {

	@Override
	public void update() {
		// TODO Auto-generated method stub
		for(Entity e : EntityManager.entitiesWithComponents(CAction.class)) {
			CAction a = e.getComponent(CAction.class);
			a.action.update();
		}
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
}
