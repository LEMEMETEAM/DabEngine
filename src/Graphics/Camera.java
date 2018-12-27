package Graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class Camera {
	protected Matrix4f view;
    protected Vector3f position;

    public Camera(){
        view = new Matrix4f().ortho(-1, 1, -1, 1, -1, 1);
        position = new Vector3f(0, 0, 0);

    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void addPosition(Vector3f position){
        this.position.add(position);
    }

    public Vector3f getPosition() {
        return position;
    }
    
    public abstract Matrix4f getProjection();
    
    public void clampCamera(Vector2f minmaxx, Vector2f minmaxy) {
    	float clamp_x = Math.max(minmaxx.x(), Math.min(minmaxx.y(), position.x()));
    	float clamp_y = Math.max(minmaxy.x(), Math.min(minmaxy.y(), position.y()));
    	position.x = clamp_x;
    	position.y = clamp_y;
    }
}
