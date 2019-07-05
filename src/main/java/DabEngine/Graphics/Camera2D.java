package DabEngine.Graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera2D extends Camera{
	
	public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void addPosition(Vector3f position){
        this.position.add(position);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void rotate(Vector3f axis, float angle){
        front.rotateAxis(angle, axis.x, axis.y, axis.z);
        up.rotateAxis(angle, axis.x, axis.y, axis.z);
    }

    public void zoom(float zoom){
        this.zoom = zoom;
    }
    
}
