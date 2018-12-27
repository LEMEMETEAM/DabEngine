package Entities.Creatures;

import org.joml.Vector4f;

import Graphics.Shaders;
import Graphics.Models.Renderable2D;

public abstract class Creature2D extends Renderable2D{
	
	protected float health;
	
	public Creature2D(float width, float height, Vector4f color, boolean center_anchor) {
		super(width, height, color, center_anchor);
		// TODO Auto-generated constructor stub
	}
	
	public void setHealth(float health) {
		this.health = health;
	}
	
	public float getHealth() {
		return health;
	}
	
	public void negateHealth(float amount) {
		health-=amount;
	}
	
	public void increaseHealth(float amount) {
		health+=amount;
	}
}
