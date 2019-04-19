package Entities.Components;

import org.joml.Vector3f;

public class CTransform extends Component {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3042942043608586977L;
	public Vector3f pos;
	public Vector3f size;
	
	public CTransform() {
		
		pos = new Vector3f();
		size = new Vector3f();
	}
}
