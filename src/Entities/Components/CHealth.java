package Entities.Components;

public class CHealth extends Component {
	private float health;

	public void increaseHealth() {
		health++;
	}
	
	public void decreaseHealth() {
		health--;
	}
}
