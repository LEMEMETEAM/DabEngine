package Graphics.Models;

import org.joml.AABBf;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public abstract class Model {

    private Mesh mesh;
    private Matrix4f model;
    private Vector3f position;
    private float scale;
    private AABB bounds;

    public Model(Mesh mesh){
        this.mesh = mesh;
        model = new Matrix4f();
        position = new Vector3f(0, 0, 0);
        scale = 1;
        bounds = new AABB(new Vector2f(position.x, position.y));
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        bounds.setCenter(new Vector2f(position.x, position.y));
    }

    public void addPosition(Vector3f position){
        this.position.add(position);
        bounds.addToCenter(new Vector2f(position.x, position.y));
    }

    public void setScale(float scale) {
        this.scale = scale;
        bounds.setHalf_extent(new Vector2f().mul(scale));
    }

    public void addScale(float scale){
        this.scale += scale;
        bounds.setHalf_extent(new Vector2f().mul(scale));
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getScale() {
        return scale;
    }

    public Matrix4f getProjection() {
        model.identity().translate(position).scale(scale);

        return model;
    }

    public boolean collision(Model m){
        if(bounds.intersects(m.getBounds())){
            return true;
        }
        return false;
    }

    public boolean collision(AABB bounds){
        if(this.bounds.intersects(bounds)){
            return true;
        }
        return false;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public AABB getBounds() {
        return bounds;
    }

    public abstract void render();

    public abstract void update();
}
