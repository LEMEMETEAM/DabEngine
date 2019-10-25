package DabEngine.Entities.Components;

import org.joml.Vector4f;

import DabEngine.Resources.Font.*;
import DabEngine.Resources.Shaders.*;
import DabEngine.Utils.Color;

public class CText extends Component {
	private static final long serialVersionUID = -9163490453753107109L;
	public String text;
	public Color color;
	public Font font;
	public Shaders textShader;
}
