package Graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera3D extends Camera{
	
	private Vector3f front = new Vector3f();
	private Vector3f up = new Vector3f(0, 1, 0);
	
	public void setPosition(Vector3f position) {
        this.position = position;
    }
	
    public Vector3f getPosition() {
        return position;
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
	
	public Matrix4f getProjection() {
        view.identity();
        
        view.lookAt(position, position.add(front, new Vector3f()), up);
        
        return view;
    }
    
}
