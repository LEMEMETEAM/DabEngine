package DabEngine.System;

import org.joml.Vector2f;

import DabEngine.Entities.Entity;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CCollision;
import DabEngine.Graphics.Graphics;
import DabEngine.Observer.EventManager;
import DabEngine.Utils.Pair;

public class CollisionSystem extends ComponentSystem {

	@Override
	public void update() {
		// TODO Auto-generated method stub
		for(Entity e : EntityManager.entitiesWithComponents(CCollision.class)) {
			for(Entity e2 : EntityManager.entitiesWithComponents(CCollision.class)) {
				if(e2 == e) {
					continue;
				}
				CCollision c = e.getComponent(CCollision.class);
				CCollision c2 = e2.getComponent(CCollision.class);
				Pair<Boolean, Vector2f> info = c2.bounds.intersects(c.bounds);
				if(info.left) {
					EventManager.INSTANCE.submitEvent(new CollisionEvent(new Pair<>(e, e2), info.right));
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
}