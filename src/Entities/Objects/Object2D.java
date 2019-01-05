package Entities.Objects;

import Core.Engine;
import Graphics.Shaders;
import Graphics.Models.Renderable2D;
import Graphics.Models.Texture;
import Input.KeyBoard;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public abstract class Object2D extends Renderable2D {

	protected boolean destroyable;
	protected boolean destroyed = false;
	protected boolean canDamage;
	protected boolean solid;
	
	public Object2D(float width, float height, Vector4f color, boolean center_anchor) {
		super(width, height, color, center_anchor);
		// TODO Auto-generated constructor stub
	}
	
	public void setDestroyable(boolean destroyable) {
		this.destroyable = destroyable;
	}
	
	public boolean isDestroyable() {
		return destroyable;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}
	
	public void setCanDamage(boolean canDamage) {
		this.canDamage = canDamage;
	}
	
	public boolean canDamage() {
		return canDamage;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public void setSolid(boolean solid) {
		this.solid = solid;
	}

}
