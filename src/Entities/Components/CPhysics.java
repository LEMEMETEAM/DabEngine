package Entities.Components;

import org.joml.Vector3f;

public class CPhysics extends Component {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4832863488845120591L;
	public Vector3f velocity = new Vector3f();
	public float MASS = 6f;
	public float MAX_VELOCITY = 10;
	public enum BodyType {
		DYNAMIC, STATIC
	}
	public BodyType bodytype;
}
