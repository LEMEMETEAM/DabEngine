package DabEngine.Graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera2D extends Camera{

    public Camera2D(){

        this.near = 0;
    }

    public Camera2D(float viewportwidth, float viewportheight){
        this.viewportWidth = viewportwidth;
        this.viewportHeight = viewportheight;
        this.near = 0;
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

    public void rotate(Vector3f axis, float angle){
        front.rotateAxis(angle, axis.x, axis.y, axis.z);
        up.rotateAxis(angle, axis.x, axis.y, axis.z);
    }

    public void zoom(float zoom){
        this.zoom = zoom;
    }

    @Override
    public Matrix4f getProjection() {
        projection.setOrtho(-viewportWidth/2, viewportWidth/2, viewportHeight/2, -viewportHeight/2, near, far);
        view.setLookAt(position, position.add(front, new Vector3f()), up);
        combined.set(projection.mul(view, new Matrix4f()));

        frustum.set(combined.invert(new Matrix4f()));
        
        return combined;
    }
    
}
