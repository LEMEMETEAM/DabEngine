package DabEngine.Graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera3D extends Camera{

    private float fovY;

    public Camera3D(float fovY, float viewportWidth, float viewportHeight){
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.fovY = fovY;
    }
	
	public void setPosition(Vector3f position) {
        this.position = position;
    }
    
    public void setFront(Vector3f front) {
    	this.front = front;
    }
    
    public Vector3f getFront() {
        return front;
    }
    
    public void strafeLeft(float speed) {
    	position.sub(front.cross(up, new Vector3f()).normalize().mul(speed, new Vector3f()));
    }
    
    public void strafeRight(float speed) {
    	position.add(front.cross(up, new Vector3f()).normalize().mul(speed, new Vector3f()));
    }
    
    public void moveForward(float speed) {
    	position.add(front.mul(speed, new Vector3f()));
    }
    
    public void moveBackward(float speed) {
    	position.sub(front.mul(speed, new Vector3f()));
    }
    
    public void rotate(double yaw, double pitch) {
    	Vector3f front = new Vector3f();
    	
    	front.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
    	front.y = (float) Math.sin(Math.toRadians(pitch));
    	front.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
    	
    	this.front = front.normalize(new Vector3f());
    }

    @Override
    public Matrix4f getProjection() {
        float aspect = viewportWidth / viewportHeight;
        projection.setPerspective(fovY, aspect, near, far);
        view.setLookAt(position, position.add(front, new Vector3f()), up);
        combined.set(projection.mul(view, new Matrix4f()));

        frustum.set(combined.invert(new Matrix4f()));
        
        return combined;
    }
    
}
