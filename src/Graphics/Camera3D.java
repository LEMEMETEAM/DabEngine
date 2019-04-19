package Graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera3D extends Camera{
	
	private Vector3f rotation = new Vector3f();
	
	public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void addPosition(Vector3f position){
        if(position.z != 0) {
        	this.position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * position.z;
        	this.position.z += (float)Math.cos(Math.toRadians(rotation.y)) * position.z;
        }
        if(position.x != 0) {
        	this.position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * position.x;
        	this.position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * position.x;
        }
        this.position.y += position.y;
    }

    public Vector3f getPosition() {
        return position;
    }
    
    public void setRotation(Vector3f rotation) {
    	this.rotation = rotation;
    }
    
    public void addRotation(Vector3f rotation) {
    	this.rotation.add(rotation);
    }
	
	public Matrix4f getProjection() {
        view.identity();
        
        view.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
        .rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        
        view.translate(position.negate(new Vector3f()));
        
        return view;
    }
    
}
