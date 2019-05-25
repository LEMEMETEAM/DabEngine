package DabEngine.Entities.Components;

import org.joml.Vector4f;

import Graphics.Models.Texture;

public class CSprite extends Component {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 183306832993313802L;
	public Texture texture;
	public Vector4f color;
	public boolean center_anchor;
}
