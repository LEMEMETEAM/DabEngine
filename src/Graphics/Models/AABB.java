package Graphics.Models;

import org.joml.Vector2f;

import Entities.Components.CCollision;
import Entities.Components.CRender;
import Entities.Components.CTransform;
import Utils.Pair;

public class AABB {

    private Vector2f center, half_extent;
    public static enum Direction{
    	UP,RIGHT,DOWN,LEFT
    }
    
    @SuppressWarnings("unused")
	private Vector2f[] compass = {
    	new Vector2f(0.0f, 1.0f).normalize(),	//up
    	new Vector2f(1.0f, 0.0f).normalize(), 	//right
    	new Vector2f(0.0f, -1.0f).normalize(),	//down
    	new Vector2f(-1.0f, 0.0f).normalize()	//left
    };

    public AABB(){
        this.center = new Vector2f(0);
        this.half_extent = new Vector2f(0);
    }

    public AABB(Vector2f center, Vector2f half_extent){
        this.center = center;
        this.half_extent = half_extent;
    }

    public AABB(Vector2f center){
        this.center = center;
        this.half_extent = new Vector2f(0);
    }

    /*public Tuple3<Boolean, Direction, Vector2f> intersects(AABB bounds2){
        Vector2f distance = bounds2.center.sub(center, new Vector2f());
        distance.x = (float) Math.abs(distance.x);
        distance.y = (float) Math.abs(distance.y);

        distance.sub(half_extent.add(bounds2.half_extent, new Vector2f()));
        float max = 0f;
        int best_match = -1;
        
        if(distance.x < 0 && distance.y < 0) {
	        for(int i = 0; i < 4; i++) {
	        	System.out.println(distance);
	        	float dot = distance.normalize().dot(compass[i]);
	        	System.out.println(dot);
	        	if(dot > max) {
	        		max = dot;
	        		best_match = i;
	        	}
	        }
	        System.out.println(Direction.values()[best_match]);
	        System.out.println("collision");
	        return Direction.values()[best_match];
        }
        return null;
    	Vector2f distance = bounds2.center.sub(center, new Vector2f());
    	Vector2f bounds2_negative = bounds2.half_extent.negate(new Vector2f());
    	float clampx = Math.max(bounds2_negative.x, Math.min(bounds2.half_extent.x, distance.x));
    	float clampy = Math.max(bounds2_negative.y, Math.min(bounds2.half_extent.y, distance.y));
    	Vector2f clamped = new Vector2f(clampx, clampy);
    	Vector2f closest = bounds2.center.sub(clamped, new Vector2f());
    	distance = closest.sub(center);
    	
    	float max = 0f;
        int best_match = -1;
    	
    	if(distance.length() < half_extent.x || distance.length() < half_extent.y) {
    		for(int i = 0; i < 4; i++) {
	        	float dot = distance.normalize().dot(compass[i]);
	        	if(dot > max) {
	        		max = dot;
	        		best_match = i;
	        	}
	        }
    		if(best_match == -1) {
    			return new Tuple3<>(false, Direction.UP, new Vector2f(0));
    		}else {
    			return new Tuple3<>(true, Direction.values()[best_match], distance);
    		}
    	}
    	return new Tuple3<>(false, Direction.UP, new Vector2f(0));
    }*/
    
    public Pair<Boolean, Vector2f> intersects(AABB bounds2){
    	Vector2f distance = bounds2.center.sub(center, new Vector2f());
        distance.x = (float) Math.abs(distance.x);
        distance.y = (float) Math.abs(distance.y);

        distance.sub(half_extent.add(bounds2.half_extent, new Vector2f()));
        
        return new Pair<>(distance.x < 0 && distance.y < 0, distance);
    }

    public Vector2f getCenter() {
        return center;
    }

    public Vector2f getHalf_extent() {
        return half_extent;
    }

    public void setCenter(Vector2f center) {
        this.center = center;
    }

    public void setHalf_extent(Vector2f half_extent) {
        this.half_extent = half_extent;
    }

    public void addToCenter(Vector2f center) {
        this.center.add(center);
    }

    public void addToHalf_extent(Vector2f half_extent) {
        this.half_extent.add(half_extent);
    }
}
