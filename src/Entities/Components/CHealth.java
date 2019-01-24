package Entities.Components;

public class CHealth extends Component {
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
