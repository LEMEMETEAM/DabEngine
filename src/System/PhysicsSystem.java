package System;

import java.lang.ref.WeakReference;

import org.joml.Vector2f;

import Entities.GameObject;
import Entities.Components.CCollision;
import Entities.Components.CPhysics;
import Entities.Components.CPhysics.BodyType;
import Entities.Components.CTransform;
import Utils.Pair;

public class PhysicsSystem extends System {
	
	public static float GRAVITY;
	
	public PhysicsSystem() {
		types.add(CPhysics.class);
		types.add(CCollision.class);
		types.add(CTransform.class);
	}
	
	private Vector2f computeGravityForce(GameObject object) {
		return new Vector2f(0, object.getComponent(CPhysics.class).MASS * GRAVITY); 
	}
	
	private void reactToGravity(GameObject object) {
		CPhysics phys = object.getComponent(CPhysics.class);
		/*
		 * FORCE = MASS * ACC
		 * ACC = FORCE / MASS
		 */
		Vector2f gravityforce = computeGravityForce(object);
		phys.addVelocity(gravityforce.x / phys.MASS, gravityforce.y / phys.MASS);
	}
	
	private void computeVelocity(GameObject object) {
		CPhysics phys = object.getComponent(CPhysics.class);
		CTransform trans = object.getComponent(CTransform.class);
		
		trans.addPos(phys.velocity.x, phys.velocity.y);
	}
	
	private void collisionDetection(int index, GameObject object1) {
		for(int y = index + 1; y < obj.size(); y++) {
			GameObject object2 = obj.get(y).get();
			Pair<Boolean, Vector2f> info = object1.getComponent(CCollision.class).getBounds().intersects(object2.getComponent(CCollision.class).getBounds());
			if(info.left) {
				collisionResolution(object1, object2, info.right);
			}
		}
	}
	
	private void collisionResolution(GameObject object1, GameObject object2, Vector2f cor) {
		CPhysics object1p = object1.getComponent(CPhysics.class);
		CPhysics object2p = object2.getComponent(CPhysics.class);
		
		CCollision object1c = object1.getComponent(CCollision.class);
		CCollision object2c = object2.getComponent(CCollision.class);
		
		if(object1p.getBodyType() == BodyType.DYNAMIC && object2p.getBodyType() == BodyType.STATIC) {
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
		else if(object1p.getBodyType() == BodyType.STATIC && object2p.getBodyType() == BodyType.DYNAMIC) {
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
	public void update() {
		// TODO Auto-generated method stub
		for(int x = 0; x < obj.size(); x++) {
			WeakReference<GameObject> object = obj.get(x);
			reactToGravity(object.get());
			collisionDetection(x, object.get());
			computeVelocity(object.get());
			//TODO implement acceleration
		}
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
}
