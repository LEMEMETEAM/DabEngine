package Entities.Components;

import org.joml.Vector2f;

public class CTransform extends Component {
	
	public Vector2f pos;
	public Vector2f size;
	
	public CTransform() {
		
		pos = new Vector2f();
		size = new Vector2f();
	}
}
