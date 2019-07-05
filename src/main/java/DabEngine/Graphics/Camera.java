package DabEngine.Graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class Camera {
	protected Matrix4f view;
    protected Vector3f position;
    protected Vector3f front;
    protected Vector3f up;
    protected float zoom = 1;

    public Camera(Vector3f position, Vector3f front, Vector3f up){
        this.position = position;
        this.front = front;
        this.up = up;
        view = new Matrix4f();
    }

    public Camera(){
        this(new Vector3f(0, 0, 0), new Vector3f(0,0,-1), new Vector3f(0,1,0));
    }
    
    public Matrix4f getProjection(){

        view.identity();
        
        view.lookAt(position, position.add(front, new Vector3f()), up);

        view.scale(zoom);
        
        return view;
    }
}
