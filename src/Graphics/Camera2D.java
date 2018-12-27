package Graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera2D extends Camera{
	
	public Matrix4f getProjection() {
        view.identity();
        
        view.translate(position.negate(new Vector3f()));
        
        return view;
    }
    
}
