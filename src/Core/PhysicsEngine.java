package Core;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.joml.Vector2f;

import Entities.PhysicsBody;
import Entities.PhysicsBody.BodyType;
import Graphics.Models.AABB;
import Utils.Pair;

public class PhysicsEngine {
	public static float GRAVITY;
	private ArrayList<PhysicsBody> objects = new ArrayList<>();
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public void addToPhysics(PhysicsBody phys) {
		objects.add(phys);
	}
	
	private void reactToGravity() {
		for(PhysicsBody object : objects) {
			if(object.getBodyType() != BodyType.STATIC)
				object.addVely(GRAVITY / object.MASS);
		}
	}
	
	private float calculateVelocityWithMomentum(float mass1, float vel1, float mass2, float vel2) {
		float totalMomentumBefore = (mass1 * vel1) + (mass2 * vel2);
		float totalMomentumAfter = totalMomentumBefore;
		float combinedMass = mass1 + mass2;
		return totalMomentumAfter / combinedMass;
	}
	
	private void reactToVelocity() {
		for(PhysicsBody obj : objects) {
			if(obj.getBodyType() != BodyType.STATIC) {
				obj.addPosition(obj.getVelx(), obj.getVely());
			}
		}
	}
	
	private void detectCollision() {
		for(int x = 0; x < objects.size() - 1; x++) {
			for(int y = x + 1; y < objects.size(); y++) {
				PhysicsBody body1 = objects.get(x);
				PhysicsBody body2 = objects.get(y);
				
				AABB bounds1 = body1.getBounds();
				AABB bounds2 = body2.getBounds();
				Pair<Boolean, Vector2f> info = bounds1.intersects(bounds2);
				if(info.left) {
					if(body1.isSolid() && body2.isSolid()) {
						resolveCollision(body1, body2, info.right);
					}
				}
			}
		}
	}
	
	private void resolveCollision(PhysicsBody body1, PhysicsBody body2, Vector2f distance) {
		distance.x = Math.abs(distance.x());
		distance.y = Math.abs(distance.y());
		if(body1.getBodyType() == BodyType.DYNAMIC && body2.getBodyType() == BodyType.DYNAMIC) {
			if(distance.x < distance.y) {
				float momentum = calculateVelocityWithMomentum(body1.MASS, body1.getVelx(), body2.MASS, body2.getVelx());
				body1.setVelx(momentum);
				body2.setVelx(momentum);
			} else {
				float momentum = calculateVelocityWithMomentum(body1.MASS, body1.getVely(), body2.MASS, body2.getVely());
				body1.setVely(momentum);
				body2.setVely(momentum);
			}
		}
		else if(body1.getBodyType() == BodyType.DYNAMIC && body2.getBodyType() == BodyType.STATIC) {
			Vector2f diff = body2.getBounds().getCenter().sub(body1.getBounds().getCenter(), new Vector2f());
			if(distance.x < distance.y) {
				if(diff.x > 0) {
					body1.setVelx(0);
					body1.addPosition(-distance.x, 0);
				} else {
					body1.setVelx(0);
					body1.addPosition(distance.x, 0);
				}
			} else {
				if(diff.y > 0) {
					body1.setVely(0);
					body1.addPosition(0, -distance.y);
				} else {
					body1.setVely(0);
					body1.addPosition(0, distance.y);
				}
			}
		}
		else if(body1.getBodyType() == BodyType.STATIC && body2.getBodyType() == BodyType.DYNAMIC) {
			Vector2f diff = body1.getBounds().getCenter().sub(body2.getBounds().getCenter(), new Vector2f());
			if(distance.x < distance.y) {
				if(diff.x > 0) {
					body2.setVelx(0);
					body2.addPosition(-distance.x, 0);
				} else {
					body2.setVelx(0);
					body2.addPosition(distance.x, 0);
				}
			} else {
				if(diff.y > 0) {
					body2.setVely(0);
					body2.addPosition(0, -distance.y);
				} else {
					body2.setVely(0);
					body2.addPosition(0, distance.y);
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
