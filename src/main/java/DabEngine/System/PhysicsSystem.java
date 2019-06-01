/* package DabEngine.System;

import org.joml.Vector3f;

import DabEngine.Entities.Entity;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CCollision;
import DabEngine.Entities.Components.CPhysics;
import DabEngine.Entities.Components.CPhysics.BodyType;
import DabEngine.Entities.Components.CTransform;
import DabEngine.Graphics.Graphics;

public class PhysicsSystem extends ComponentSystem {
	
	public static float GRAVITY;
	
	private Vector3f computeGravityForce(Entity object) {
		return new Vector3f(0, object.getComponent(CPhysics.class).MASS * GRAVITY, 0); 
	}
	
	private void reactToGravity(Entity object) {
		CPhysics phys = object.getComponent(CPhysics.class);
		
		Vector3f gravityforce = computeGravityForce(object);
		phys.velocity.add(gravityforce.x / phys.MASS, gravityforce.y / phys.MASS, gravityforce.z / phys.MASS);
	}
	
	private void computeVelocity(Entity object) {
		CPhysics phys = object.getComponent(CPhysics.class);
		CTransform trans = object.getComponent(CTransform.class);
		
		trans.pos.add(phys.velocity.x, phys.velocity.y, phys.velocity.z);
	}
	
	private void collisionResolution() {
		CollisionEvent collision = EventManager.receiveEvent(CollisionEvent.class);
		Vector3f cor = collision.diff;
		
		CPhysics object1p = collision.entities.left.getComponent(CPhysics.class);
		CPhysics object2p = collision.entities.right.getComponent(CPhysics.class);
		
		CCollision object1c = collision.entities.left.getComponent(CCollision.class);
		CCollision object2c = collision.entities.right.getComponent(CCollision.class);
		
		if(object1p.bodytype == BodyType.DYNAMIC && object2p.bodytype == BodyType.STATIC) {
			Vector3f dir = object1c.bounds.getCenter().sub(object2c.bounds.getCenter());
			if(cor.x > cor.y && cor.x > cor.z) {
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
		for(Entity e : EntityManager.entitiesWithComponents(CPhysics.class, CTransform.class, CCollision.class)) {
			reactToGravity(e);
			computeVelocity(e);
			//TODO implement acceleration
		}
		if(EventManager.hasEvent(CollisionEvent.class)) {
			collisionResolution();
		}
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
}
 */