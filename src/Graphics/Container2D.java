package Graphics;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector4f;

import Entities.GameObject;

public class Container2D {
	
	private final ArrayList<GameObject> items = new ArrayList<>();
	private Vector2f pos = new Vector2f();
	private Vector4f tint = new Vector4f(1);
	private float scale;
	
	public Container2D add(GameObject item) {
		items.add(item);
		return this;
	}
	
	public Container2D remove(int index) {
		items.remove(index);
		return this;
	}
	
	public Container2D removeFirst(GameObject item) {
		items.remove(item);
		return this;
	}
	
	public void setPosition(Vector2f pos) {
		float distancex = this.pos.distance(pos.x(), 0);
		float distancey = this.pos.distance(0, pos.y());
		for(GameObject item : items) {
			item.addPosition(distancex, distancey);
		}
		this.pos = pos;
	}
	
	public void addPosition(Vector2f pos) {
		for(GameObject item : items) {
			item.addPosition(pos);
		}
		this.pos.add(pos);
	}
	
	public void setScale(float scale) {
		float scale_delta = this.scale - scale;
		for(GameObject item : items) {
			item.addScale(scale_delta);
		}
		this.scale = scale;
	}
	
	public void addScale(float scale) {
		for(GameObject item : items) {
			item.addScale(scale);
		}
		this.scale += scale;
	}
	
	public void tint(Vector4f tint) {
		float r_delta = this.tint.x() - tint.x();
		float g_delta = this.tint.y() - tint.y();
		float b_delta = this.tint.z() - tint.z();
		float a_delta = this.tint.w() - tint.w();
		for(GameObject item : items) {
			item.addColor(new Vector4f(r_delta, g_delta, b_delta, a_delta));
		}
		this.tint = tint;
	}
}
