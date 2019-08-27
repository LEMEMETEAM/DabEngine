package DabEngine.Entities.Components;

import DabEngine.Graphics.Models.UniformBuffer;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;

public class CShader extends Component {
    public Shaders shader;
    public UniformBuffer bufferedUniforms;
}