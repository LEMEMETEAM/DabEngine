package System;

import org.joml.Vector3f;

import Entities.Entity;
import Entities.EntityManager;
import Entities.Components.CCollision;
import Graphics.Graphics;
import Observer.Event;
import Utils.Pair;

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
				Pair<Boolean, Vector3f> info = c2.bounds.intersects(c.bounds);
				if(info.left) {
					EventManager.submitEvent(new CollisionEvent(new Pair<>(e, e2), info.right));
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
}

class CollisionEvent implements Event {
	public Pair<Entity, Entity> entities;
	public Vector3f diff;
	
	public CollisionEvent(Pair<Entity, Entity> entities, Vector3f diff) {
		this.entities = entities;
		this.diff = diff;
	}
}