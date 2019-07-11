package DabEngine.Graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.FrustumIntersection;

public abstract class Camera {
    protected Matrix4f view = new Matrix4f();
    protected Matrix4f projection = new Matrix4f();
    protected Matrix4f combined = new Matrix4f();

    protected Vector3f position = new Vector3f(0,0,1);
    protected Vector3f front = new Vector3f(0,0,-1);
    protected Vector3f up = new Vector3f(0,1,0);

    protected float zoom = 1;
    protected float near = 1;
    protected float far = 100;

    protected float viewportWidth, viewportHeight;

    protected FrustumIntersection frustum = new FrustumIntersection();
    
    public abstract Matrix4f getProjection();

    public Vector2f screenToWorld(Vector2f s, float viewportwidth, float viewportheight){
        Matrix4f inv = getProjection().invert(new Matrix4f());

        Vector4f pos = new Vector4f();
        Vector4f NDC = new Vector4f(
            (2.0f*((float)s.x/viewportwidth))-1.0f,
            1.0f - (2.0f*((float)s.y/viewportheight)),
            2.0f*0-1.0f,
            1.0f
        );
        pos = NDC.mul(inv, new Vector4f());
        
        pos.w = 1.0f / pos.w;
        pos.x *= pos.w;
        pos.y *= pos.w;
        pos.z *= pos.w;

        return new Vector2f(pos.x, pos.y);
    }
}
