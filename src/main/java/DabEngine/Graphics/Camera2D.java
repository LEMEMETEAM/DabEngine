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
	
	public Matrix4f getProjection() {
        view.identity();
        
        view.translate(position.negate(new Vector3f()));
        
        return view;
    }
    
}
