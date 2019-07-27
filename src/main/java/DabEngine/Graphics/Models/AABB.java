package DabEngine.Graphics.Models;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import DabEngine.Graphics.Graphics;
import DabEngine.Utils.Pair;
import static org.lwjgl.opengl.GL33.*;


public class AABB {

    public Vector3f center, half_extent;

    public AABB(){
        this.center = new Vector3f(0);
        this.half_extent = new Vector3f(0);
    }

    public AABB(Vector3f center, Vector3f half_extent){
        this.center = center;
        this.half_extent = half_extent;
    }

    public AABB(Vector3f center){
        this.center = center;
        this.half_extent = new Vector3f(0);
    }

    /*public Tuple3<Boolean, Direction, Vector3f> intersects(AABB bounds2){
        Vector3f distance = bounds2.center.sub(center, new Vector3f());
        distance.x = (float) Math.abs(distance.x);
        distance.y = (float) Math.abs(distance.y);

        distance.sub(half_extent.add(bounds2.half_extent, new Vector3f()));
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
    	Vector3f distance = bounds2.center.sub(center, new Vector3f());
    	Vector3f bounds2_negative = bounds2.half_extent.negate(new Vector3f());
    	float clampx = Math.max(bounds2_negative.x, Math.min(bounds2.half_extent.x, distance.x));
    	float clampy = Math.max(bounds2_negative.y, Math.min(bounds2.half_extent.y, distance.y));
    	Vector3f clamped = new Vector3f(clampx, clampy);
    	Vector3f closest = bounds2.center.sub(clamped, new Vector3f());
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
    			return new Tuple3<>(false, Direction.UP, new Vector3f(0));
    		}else {
    			return new Tuple3<>(true, Direction.values()[best_match], distance);
    		}
    	}
    	return new Tuple3<>(false, Direction.UP, new Vector3f(0));
    }*/
    
    public Vector3f intersects(AABB bounds2){
    	Vector3f distance = bounds2.center.sub(center, new Vector3f());
        distance.x = (float) Math.abs(distance.x);
        distance.y = (float) Math.abs(distance.y);
        distance.z = (float) Math.abs(distance.z);

        distance.sub(half_extent.add(bounds2.half_extent, new Vector3f()));

        if(!(distance.x < 0 && distance.y < 0 && distance.z < 0)){
            return new Vector3f(Float.NaN);
        }
        
        return distance;
    }
    
    /*public Pair<Boolean, Vector3f> intersects(AABB bounds2){
    	Vector3f distance = bounds2.center.sub(center, new Vector3f());
        distance.x = (float) Math.abs(distance.x);
        distance.y = (float) Math.abs(distance.y);

        distance.sub(half_extent.add(bounds2.half_extent, new Vector3f()));
        
        return new Pair<>(distance.x < 0 && distance.y < 0, distance);
    }*/

    public void debugDraw(Graphics g){
        glMatrixMode(GL_PROJECTION);
        glLoadMatrixf(g.getBatch().projectionMatrix.get(BufferUtils.createFloatBuffer(16)));
        glUseProgram(0);
        glColor3f(1,0,0);
        glBegin(GL_LINE_LOOP);
            glVertex3f(center.x - half_extent.x, center.y - half_extent.y, center.z + half_extent.z);
            glVertex3f(center.x + half_extent.x, center.y - half_extent.y, center.z + half_extent.z);
            glVertex3f(center.x + half_extent.x, center.y + half_extent.y, center.z + half_extent.z);
            glVertex3f(center.x - half_extent.x, center.y + half_extent.y, center.z + half_extent.z);
        glEnd();
        glBegin(GL_LINE_LOOP);
            glVertex3f(center.x - half_extent.x, center.y - half_extent.y, center.z + half_extent.z);
            glVertex3f(center.x - half_extent.x, center.y + half_extent.y, center.z + half_extent.z);
            glVertex3f(center.x - half_extent.x, center.y + half_extent.y, center.z - half_extent.z);
            glVertex3f(center.x - half_extent.x, center.y - half_extent.y, center.z - half_extent.z);
        glEnd();
        glBegin(GL_LINE_LOOP);
            glVertex3f(center.x + half_extent.x, center.y + half_extent.y, center.z + half_extent.z);
            glVertex3f(center.x + half_extent.x, center.y + half_extent.y, center.z - half_extent.z);
            glVertex3f(center.x - half_extent.x, center.y + half_extent.y, center.z - half_extent.z);
            glVertex3f(center.x - half_extent.x, center.y + half_extent.y, center.z + half_extent.z);
        glEnd();
        glBegin(GL_LINE_LOOP);
            glVertex3f(center.x + half_extent.x, center.y - half_extent.y, center.z + half_extent.z);
            glVertex3f(center.x + half_extent.x, center.y - half_extent.y, center.z - half_extent.z);
            glVertex3f(center.x + half_extent.x, center.y + half_extent.y, center.z - half_extent.z);
            glVertex3f(center.x + half_extent.x, center.y + half_extent.y, center.z + half_extent.z);
        glEnd();
        glBegin(GL_LINE_LOOP);
            glVertex3f(center.x - half_extent.x, center.y - half_extent.y, center.z + half_extent.z);
            glVertex3f(center.x + half_extent.x, center.y - half_extent.y, center.z + half_extent.z);
            glVertex3f(center.x + half_extent.x, center.y - half_extent.y, center.z - half_extent.z);
            glVertex3f(center.x - half_extent.x, center.y - half_extent.y, center.z - half_extent.z);
        glEnd();
        glBegin(GL_LINE_LOOP);
            glVertex3f(center.x + half_extent.x, center.y - half_extent.y, center.z - half_extent.z);
            glVertex3f(center.x - half_extent.x, center.y - half_extent.y, center.z - half_extent.z);
            glVertex3f(center.x - half_extent.x, center.y + half_extent.y, center.z - half_extent.z);
            glVertex3f(center.x + half_extent.x, center.y + half_extent.y, center.z - half_extent.z);
        glEnd();
    }
}
