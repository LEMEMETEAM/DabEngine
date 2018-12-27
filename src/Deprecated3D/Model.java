package Deprecated3D;

import org.joml.AABBf;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import Graphics.Models.AABB;

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
    private Vector3f scale;
    private AABB bounds;
    private boolean solid;

    public Model(Mesh mesh){
        this.mesh = mesh;
        model = new Matrix4f();
        position = new Vector3f(0, 0, 0);
        scale = new Vector3f(1);
        bounds = new AABB(new Vector2f(position.x, position.y));
        solid = true;
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
        this.scale.set(scale);
        bounds.setHalf_extent(new Vector2f().mul(scale));
    }
    
    public void setScale(float x, float y, float z) {
        this.scale.set(x, y, z);
        bounds.setHalf_extent(new Vector2f().mul(x, y));
    }

    public void addScale(float scale){
        this.scale.add(new Vector3f().set(scale));
        bounds.setHalf_extent(new Vector2f().mul(scale));
    }
    
    public void addScale(float x, float y, float z){
        this.scale.add(x, y, z);
        bounds.setHalf_extent(new Vector2f().mul(x, y));
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Matrix4f getProjection() {
        model.identity().translate(position).scale(scale);

        return model;
    }

    public boolean collision(Model m, AABB.Direction dir){
        if(bounds.intersects(m.getBounds()) == dir){
            return true;
        }
        return false;
    }

    public boolean collision(AABB bounds, AABB.Direction dir){
        if(this.bounds.intersects(bounds) == dir){
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
    
    public boolean isSolid() {
		return solid;
	}
    
    public void setSolid(boolean solid) {
		this.solid = solid;
	}

    public abstract void render();

    public abstract void update();
}
