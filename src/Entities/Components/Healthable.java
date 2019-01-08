package Entities.Components;

public interface Healthable extends Destroyable {
	public float health = 0;
	public void onDamage();
	public void onHealthRefil();
}
