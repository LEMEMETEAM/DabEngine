package Graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera2D {

    private Matrix4f view;
    private Vector3f position;

    public Camera2D(){
        view = new Matrix4f();
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

    public Matrix4f getProjection() {
        Matrix4f target = new Matrix4f();
        Matrix4f pos = new Matrix4f().translate(position);

        target = view.mul(pos, target);
        return target;
    }
}
