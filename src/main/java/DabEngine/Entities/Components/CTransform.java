package DabEngine.Entities.Components;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class CTransform extends Component {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3042942043608586977L;
	public Vector3f pos = new Vector3f();
	public Vector3f size = new Vector3f();
	public Vector4f rotation = new Vector4f();
	public Vector3f origin = new Vector3f(pos);
}
