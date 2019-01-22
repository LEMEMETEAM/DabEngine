package Entities;

import org.joml.*;

import Entities.Components.ComponentSystem;
import Graphics.Animation;
import Graphics.SpriteBatch;
import Graphics.Models.AABB;
import Graphics.Models.Texture;
import Input.InputHandler;

public abstract class GameObject extends ComponentSystem {

	protected float velx, vely;
	private Texture texture;
	private float width, height;
	private Vector4f color;
	private boolean center_anchor;
	private Vector4f xywh;
	private AABB bounds;
	public int MAX_VELOCITY;
	private boolean solid;
	public Animation anim = new Animation();
	public int MASS = 10;

	public GameObject(float width, float height, Vector4f color, boolean center_anchor) {
		this.width = width;
		this.height = height;

		this.color = color;

		this.center_anchor = center_anchor;

		xywh = new Vector4f(0, 0, width, height);

		bounds = new AABB();
		if (center_anchor) {
			getBounds().setCenter(getPosition());
		} else {
			getBounds().setCenter(new Vector2f(getPosition().x + (width / 2), getPosition().y + (height / 2)));
		}
		getBounds().setHalf_extent(new Vector2f(width / 2, height / 2));
	}

	public void render(SpriteBatch batch) {
		batch.draw(texture, xywh, color, center_anchor);
	}

	public void setPosition(float x, float y) {
		xywh.x = x;
		xywh.y = y;
		if (center_anchor) {
			bounds.setCenter(new Vector2f(x, y));
		} else {
			bounds.setCenter(new Vector2f(x + (width / 2), y + (height / 2)));
		}
	}

	public void setColor(Vector4f color) {
		this.color = color;
	}

	public void addColor(Vector4f color) {
		this.color.add(color);
	}

	public Vector4f getColor() {
		return color;
	}

	public void setPosition(Vector2f xy) {
		xywh.x = xy.x();
		xywh.y = xy.y();
		if (center_anchor) {
			bounds.setCenter(xy);
		} else {
			bounds.setCenter(new Vector2f(xy.x + (width / 2), xy.y + (height / 2)));
		}
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

	public Texture getTexture() {
		return texture;
	}

	public void setVelx(float velx) {
		this.velx = velx;
	}

	public void addVelx(float velx) {
		this.velx += velx;
	}

	public float getVelx() {
		return velx;
	}

	public void setVely(float vely) {
		this.vely = vely;
	}

	public void addVely(float vely) {
		this.vely += vely;
	}

	public float getVely() {
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

	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public abstract void update();

	public abstract void processInput(InputHandler handler);
}
