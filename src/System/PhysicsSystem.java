package System;

import org.joml.Vector3f;

import Entities.Entity;
import Entities.EntityManager;
import Entities.Components.CCollision;
import Entities.Components.CPhysics;
import Entities.Components.CPhysics.BodyType;
import Entities.Components.CTransform;
import Graphics.Graphics;
import Utils.Pair;

public class PhysicsSystem extends ComponentSystem {
	
	public static float GRAVITY;
	
	private Vector3f computeGravityForce(Entity object) {
		return new Vector3f(0, object.getComponent(CPhysics.class).MASS * GRAVITY, 0); 
	}
	
	private void reactToGravity(Entity object) {
		CPhysics phys = object.getComponent(CPhysics.class);
		/*
		 * FORCE = MASS * ACC
		 * ACC = FORCE / MASS
		 */
		Vector3f gravityforce = computeGravityForce(object);
		phys.velocity.add(gravityforce.x / phys.MASS, gravityforce.y / phys.MASS, gravityforce.z / phys.MASS);
	}
	
	private void computeVelocity(Entity object) {
		CPhysics phys = object.getComponent(CPhysics.class);
		CTransform trans = object.getComponent(CTransform.class);
		
		trans.pos.add(phys.velocity.x, phys.velocity.y, phys.velocity.z);
	}
	
	private void collisionDetection(Entity e2, Entity e1) {
		if(e2 == null) {
			return;
		}
		Pair<Boolean, Vector3f> info = e1.getComponent(CCollision.class).bounds.intersects(e2.getComponent(CCollision.class).bounds);
		if(info.left) {
			collisionResolution(e1, e2, info.right);
		}
	}
	
	private void collisionResolution(Entity object1, Entity object2, Vector3f cor) {
		CPhysics object1p = object1.getComponent(CPhysics.class);
		CPhysics object2p = object2.getComponent(CPhysics.class);
		
		CCollision object1c = object1.getComponent(CCollision.class);
		CCollision object2c = object2.getComponent(CCollision.class);
		
		if(object1p.bodytype == BodyType.DYNAMIC && object2p.bodytype == BodyType.STATIC) {
			Vector3f dir = object1c.bounds.getCenter().sub(object2c.bounds.getCenter());
			if(cor.x > cor.y && cor.x > cor.y) {
				if(dir.x > 0) {
					object1p.velocity.x = cor.x;
				}
				else {
					object1p.velocity.x = -cor.x;
				}
			}
			else if(cor.y > cor.x && cor.y > cor.z){
				if(dir.y > 0) {
					object1p.velocity.y = -cor.y;
				}
				else {
					object1p.velocity.y = cor.y;
				}
			}
			else {
				if(dir.z > 0) {
					object1p.velocity.z = -cor.z;
				}
				else {
					object1p.velocity.z = cor.z;
				}
			}
		}
		else if(object1p.bodytype == BodyType.STATIC && object2p.bodytype == BodyType.DYNAMIC) {
			Vector3f dir = object2c.bounds.getCenter().sub(object1c.bounds.getCenter());
			if(cor.x > cor.y && cor.x > cor.z) {
				if(dir.x > 0) {
					object2p.velocity.x = cor.x;
				}
				else {
					object2p.velocity.x = -cor.x;
				}
			}
			else if(cor.y > cor.x && cor.y > cor.z){
				if(dir.y > 0) {
					object2p.velocity.y = -cor.y;
				}
				else {
					object2p.velocity.y = cor.y;
				}
			}
			else {
				if(dir.z > 0) {
					object2p.velocity.z = -cor.z;
				}
				else {
					object2p.velocity.z = cor.z;
				}
			}
		}
	}

	@Override
	public void update() {
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
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
}
