package Entities.Components;

public class CHealth {
	private float health;

	public void increaseHealth() {
		health++;
	}
	
	public void decreaseHealth() {
		health--;
	}
	
	public void set(float health) {
		this.health = health;
	}
	
	public float get() {
		return health;
	}
}
