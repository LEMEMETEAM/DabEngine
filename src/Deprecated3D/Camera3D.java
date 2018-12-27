package Deprecated3D;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Camera3D {

    private Matrix4f view;
    private Vector3f position;
    private float pitch;
    private float yaw;

    public Camera3D(){
        view = new Matrix4f();
        position = new Vector3f(0, 0, 0);

    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void addPosition(Vector3f position){
        if ( position.z != 0 ) {
            this.position.x += (float)Math.sin(Math.toRadians(yaw)) * -1.0f * position.z;
            this.position.z += (float)Math.cos(Math.toRadians(yaw)) * position.z;
        }
        if ( position.x != 0) {
            this.position.x += (float)Math.sin(Math.toRadians(yaw - 90)) * -1.0f * position.x;
            this.position.z += (float)Math.cos(Math.toRadians(yaw - 90)) * position.x;
        }
        this.position.y += position.y;
    }

    public void setYaw(float amount) {
        this.yaw = amount;
    }

    public void addYaw(float amount){
        this.yaw += amount;
    }

    public void setPitch(float amount) {
        this.pitch = pitch;
    }

    public void addPitch(float amount){
        this.pitch += amount;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public Matrix4f getProjection() {
        //Matrix4f target = new Matrix4f();
        //Matrix4f pos = new Matrix4f().setRotationXYZ((float)Math.toRadians(-rotation.x), (float)Math.toRadians(-rotation.y), (float)Math.toRadians(rotation.z)).setTranslation(position);

        //target = view.mul(pos, target);
        //return target;

        view.identity().rotate(pitch, 1.0f, 0f, 0f).rotate(yaw, 0f, 1.0f, 0f).translate(position);

        return view;
    }
}
