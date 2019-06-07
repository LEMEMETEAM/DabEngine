package DabEngine.Entities.Components;

import DabEngine.Graphics.OpenGL.Shaders.Shaders;

/**
 * Effect component that uses shaders
 */
public class CEffect extends Component {
    private static final long serialVersionUID = -6014217970956801067L;
    
    /**
     * Shader Effect
     */
    public Shaders effect;
}