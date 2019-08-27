package DabEngine.Entities.Components;

import DabEngine.Graphics.OpenGL.Light.Light;

public class CLight extends Component {
    public static final short MAX_LIGHTS = 32;
    public Light light;
}