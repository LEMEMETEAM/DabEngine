package DabEngine.Entities.Components;

import org.joml.Vector3f;

import DabEngine.Graphics.Blending;
import DabEngine.Resources.Textures.*;

public class CParticleSpawner extends Component {

    public String particle_texture;
    public float spread;
    public float direction;
    public Blending blend_mode;
    public Vector3f force_on_particle;
    public float particleLifeTime;
    public float particle_mass;
    public Vector3f particle_size;

}