package Graphics.Models;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector4f;

import Deprecated3D.Mesh;
import Deprecated3D.Model;
import Graphics.Shaders;
import Graphics.SpriteBatch;
import Input.InputHandler;
import Observer.Observer;
import Observer.Observerable;

public abstract class Renderable2D{
	
	protected int velx, vely;
	private Shaders s;
	private Texture texture;
	private float width, height;
	private Vector4f color;
	private boolean center_anchor;
	private Vector4f xywh;
	private AABB bounds;
	private ArrayList<Observer> observers;
	
	public Renderable2D(float width, float height, Vector4f color, boolean center_anchor) {
		this.width = width;
		this.height = height;
		
		this.color = color;
		
		this.center_anchor = center_anchor;
		
		xywh = new Vector4f(0, 0, width, height);
		
		bounds = new AABB();
		bounds.setCenter(getPosition());
		bounds.setHalf_extent(new Vector2f(getWidth()/2, getHeight()/2));
		
		observers = new ArrayList<>();
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(texture, xywh, color, center_anchor);
	}
	
	public void setPosition(float x, float y) {
		xywh.x = x;
		xywh.y = y;
		bounds.setCenter(new Vector2f(x, y));
	}
	
	public void setPosition(Vector2f xy) {
		xywh.x = xy.x();
		xywh.y = xy.y();
		bounds.setCenter(xy);
	}
	
	public void addPosition(float x, float y) {
		xywh.x += x;
		xywh.y += y;
		bounds.addToCenter(new Vector2f(x, y));
	}
	
	public void addPosition(Vector2f xy) {
		xywh.x += xy.x();
		xywh.y += xy.y();
		bounds.addToCenter(xy);
	}
	
	public void addScale(float scale) {
		xywh.z *= scale;
		xywh.w *= scale;
		bounds.addToHalf_extent(new Vector2f().mul(scale));
	}
	
	public void addScale(float scalex, float scaley) {
		xywh.z *= scalex;
		xywh.w *= scaley;
		bounds.addToHalf_extent(new Vector2f().mul(scalex, scaley));
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public void setVelx(int velx) {
		this.velx = velx;
	}
	
	public int getVelx() {
		return velx;
	}
	
	public void setVely(int vely) {
		this.vely = vely;
	}
	
	public int getVely() {
		return vely;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public Vector2f getPosition() {
		return new Vector2f(xywh.x, xywh.y);
	}
	
	public AABB getBounds() {
		return bounds;
	}
	
	public boolean collision(Renderable2D m, AABB.Direction dir){
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

	public abstract void update();
	
	public abstract void processInput(InputHandler handler);
}
