package Core;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.joml.Vector2f;

import Entities.GameObject;
import Entities.Components.CCollision;
import Entities.Components.CPhysics;
import Entities.Components.CPhysics.*;
import Entities.Components.CTransform;
import Graphics.Models.AABB;
import Observer.Observer;
import Utils.Pair;

public class PhysicsEngine {
	public static float GRAVITY;
	private ArrayList<CPhysics> objects = new ArrayList<>();
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public void addToPhysics(CPhysics phys) {
		objects.add(phys);
	}
	
	private void reactToGravity() {
		for(CPhysics object : objects) {
			if(object.getBodyType() != BodyType.STATIC) {
				object.addVelocity(0, GRAVITY / object.MASS);
			}
		}
	}
	
	private float calculateVelocityWithMomentum(float mass1, float vel1, float mass2, float vel2) {
		float totalMomentumBefore = (mass1 * vel1) + (mass2 * vel2);
		float totalMomentumAfter = totalMomentumBefore;
		float combinedMass = mass1 + mass2;
		return totalMomentumAfter / combinedMass;
	}
	
	private void reactToVelocity() {
		for(CPhysics obj : objects) {
			if(obj.getBodyType() != BodyType.STATIC) {
				obj.gameObject.getComponent(CTransform.class).addPos(obj.velocity.x(), obj.velocity.y());
			}
		}
	}
	
	private void detectCollision() {
		for(int x = 0; x < objects.size() - 1; x++) {
			for(int y = x + 1; y < objects.size(); y++) {
				GameObject body1 = objects.get(x).gameObject;
				GameObject body2 = objects.get(y).gameObject;
				
				if(body1.hasComponent(CCollision.class) && body2.hasComponent(CCollision.class)) {
					AABB bounds1 = body1.getComponent(CCollision.class).getBounds();
					
					AABB bounds2 = body2.getComponent(CCollision.class).getBounds();
					
					Pair<Boolean, Vector2f> info = bounds1.intersects(bounds2);
					if(info.left) {
						resolveCollision(objects.get(x), objects.get(y), info.right);
					}
				}
			}
		}
	}
	
	private void resolveCollision(CPhysics body1, CPhysics body2, Vector2f distance) {
		distance.x = Math.abs(distance.x());
		distance.y = Math.abs(distance.y());
		if(body1.getBodyType() == BodyType.DYNAMIC && body2.getBodyType() == BodyType.DYNAMIC) {
			if(distance.x < distance.y) {
				float momentum = calculateVelocityWithMomentum(body1.MASS, body1.velocity.x(), body2.MASS, body2.velocity.x());
				body1.velocity.set(momentum, 0);
				body2.velocity.set(momentum, 0);
			} else {
				float momentum = calculateVelocityWithMomentum(body1.MASS, body1.velocity.y(), body2.MASS, body2.velocity.y());
				body1.velocity.set(0, momentum);
				body2.velocity.set(0, momentum);
			}
		}
		else if(body1.getBodyType() == BodyType.DYNAMIC && body2.getBodyType() == BodyType.STATIC) {
			CCollision col1 = body1.gameObject.getComponent(CCollision.class);
			CCollision col2 = body2.gameObject.getComponent(CCollision.class);
			Vector2f diff = col2.getBounds().getCenter().sub(col1.getBounds().getCenter(), new Vector2f());
			if(distance.x < distance.y) {
				if(diff.x > 0) {
					body1.velocity.set(0, body1.velocity.y());
					body1.gameObject.getComponent(CTransform.class).addPos(-distance.x, 0);
				} else {
					body1.velocity.set(0, body1.velocity.y());
					body1.gameObject.getComponent(CTransform.class).addPos(distance.x, 0);
				}
			} else {
				if(diff.y > 0) {
					body1.velocity.set(body1.velocity.x(), 0);
					body1.gameObject.getComponent(CTransform.class).addPos(0, -distance.y);
				} else {
					body1.velocity.set(body1.velocity.x(), 0);
					body1.gameObject.getComponent(CTransform.class).addPos(0, distance.y);
				}
			}
		}
		else if(body1.getBodyType() == BodyType.STATIC && body2.getBodyType() == BodyType.DYNAMIC) {
			CCollision col1 = (CCollision)body1.gameObject.getComponent(CCollision.class);
			CCollision col2 = (CCollision)body2.gameObject.getComponent(CCollision.class);
			Vector2f diff = col1.getBounds().getCenter().sub(col2.getBounds().getCenter(), new Vector2f());
			if(distance.x < distance.y) {
				if(diff.x > 0) {
					body2.velocity.set(0, body2.velocity.y());
					body2.gameObject.getComponent(CTransform.class).addPos(-distance.x, 0);
				} else {
					body2.velocity.set(0, body2.velocity.y());
					body2.gameObject.getComponent(CTransform.class).addPos(distance.x, 0);
				}
			} else {
				if(diff.y > 0) {
					body2.velocity.set(body2.velocity.x(), 0);
					body2.gameObject.getComponent(CTransform.class).addPos(0, -distance.y);
				} else {
					body2.velocity.set(body2.velocity.x(), 0);
					body2.gameObject.getComponent(CTransform.class).addPos(0, distance.y);
				}
			}
		}
	}
	
	public void update() {
		reactToGravity();
		reactToVelocity();
		detectCollision();
	}
}
