package System;

import java.lang.ref.WeakReference;
import java.util.Iterator;

import org.joml.Vector2f;

import Entities.Entity;
import Entities.EntityManager;
import Entities.Components.CCollision;
import Entities.Components.CPhysics;
import Entities.Components.CPhysics.BodyType;
import Entities.Components.CTransform;
import Utils.Pair;

public class PhysicsSystem extends System {
	
	public static float GRAVITY;
	
	private Vector2f computeGravityForce(Entity object) {
		return new Vector2f(0, object.getComponent(CPhysics.class).MASS * GRAVITY); 
	}
	
	private void reactToGravity(Entity object) {
		CPhysics phys = object.getComponent(CPhysics.class);
		/*
		 * FORCE = MASS * ACC
		 * ACC = FORCE / MASS
		 */
		Vector2f gravityforce = computeGravityForce(object);
		phys.velocity.add(gravityforce.x / phys.MASS, gravityforce.y / phys.MASS);
	}
	
	private void computeVelocity(Entity object) {
		CPhysics phys = object.getComponent(CPhysics.class);
		CTransform trans = object.getComponent(CTransform.class);
		
		trans.pos.add(phys.velocity.x, phys.velocity.y);
	}
	
	private void collisionDetection(Entity e2, Entity e1) {
		if(e2 == null) {
			return;
		}
		Pair<Boolean, Vector2f> info = e1.getComponent(CCollision.class).getBounds().intersects(e2.getComponent(CCollision.class).getBounds());
		if(info.left) {
			collisionResolution(e1, e2, info.right);
		}
	}
	
	private void collisionResolution(Entity object1, Entity object2, Vector2f cor) {
		CPhysics object1p = object1.getComponent(CPhysics.class);
		CPhysics object2p = object2.getComponent(CPhysics.class);
		
		CCollision object1c = object1.getComponent(CCollision.class);
		CCollision object2c = object2.getComponent(CCollision.class);
		
		if(object1p.bodytype == BodyType.DYNAMIC && object2p.bodytype == BodyType.STATIC) {
			Vector2f dir = object1c.getBounds().getCenter().sub(object2c.getBounds().getCenter());
			if(cor.x > cor.y) {
				if(dir.x > 0) {
					object1p.velocity.x = cor.x;
				}
				else {
					object1p.velocity.x = -cor.x;
				}
			}
			else {
				if(dir.y > 0) {
					object1p.velocity.y = -cor.y;
				}
				else {
					object1p.velocity.y = cor.y;
				}
			}
		}
		else if(object1p.bodytype == BodyType.STATIC && object2p.bodytype == BodyType.DYNAMIC) {
			Vector2f dir = object2c.getBounds().getCenter().sub(object1c.getBounds().getCenter());
			if(cor.x > cor.y) {
				if(dir.x > 0) {
					object2p.velocity.x = cor.x;
				}
				else {
					object2p.velocity.x = -cor.x;
				}
			}
			else {
				if(dir.y > 0) {
					object2p.velocity.y = -cor.y;
				}
				else {
					object2p.velocity.y = cor.y;
				}
			}
		}
	}

	@Override
	public void update(double delta) {
		// TODO Auto-generated method stub
		Entity e1;
		Entity e2 = null;
		for(Entity e : EntityManager.entitiesWithComponents(CPhysics.class, CTransform.class, CCollision.class)) {
			e1 = e;
			reactToGravity(e1);
			collisionDetection(e2, e1);
			computeVelocity(e1);
			//TODO implement acceleration
			e2 = e;
		}
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
}
