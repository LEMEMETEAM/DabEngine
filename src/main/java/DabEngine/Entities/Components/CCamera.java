package DabEngine.Entities.Components;

import DabEngine.Graphics.Camera;
import DabEngine.Resources.RenderTarget;

public class CCamera extends Component {
    public Camera camera;
    public float pitch, yaw;
    public RenderTarget renderTarget;
}