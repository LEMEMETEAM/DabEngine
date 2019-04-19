package Graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class Camera {
	protected Matrix4f view;
    protected Vector3f position;

    public Camera(){
        position = new Vector3f(0);
        view = new Matrix4f();
    }
    
    public abstract Matrix4f getProjection();
    
    public void clampCamera(Vector2f minmaxx, Vector2f minmaxy, Vector2f minmaxz) {
    	float clamp_x = Math.max(minmaxx.x(), Math.min(minmaxx.y(), position.x()));
    	float clamp_y = Math.max(minmaxy.x(), Math.min(minmaxy.y(), position.y()));
    	float clamp_z = Math.max(minmaxz.x(), Math.min(minmaxz.y(), position.y()));
    	position.x = clamp_x;
    	position.y = clamp_y;
    	position.z = clamp_z;
    }
}
