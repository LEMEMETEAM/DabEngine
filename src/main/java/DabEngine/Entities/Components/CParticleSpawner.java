package DabEngine.Entities.Components;

import org.jruby.util.Random;

import DabEngine.Graphics.OpenGL.Blending;
import DabEngine.Graphics.OpenGL.Textures.Texture;

public class CParticleSpawner extends Component {

    public Texture particle_texture;
    public float spread;
    public float direction;
    public Blending blend_mode;
    public float force_on_particle;
    public float particleLifeTime;
    public final Random rng = new Random();

}