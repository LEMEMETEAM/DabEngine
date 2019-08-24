package DabEngine.Graphics.OpenGL.Light;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import DabEngine.Graphics.Models.UniformAttribs;
import DabEngine.Graphics.Models.UniformBuffer;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Utils.Color;

public class Light {

    public Vector3f pos;
    public Vector3f color;
    public static UniformBuffer lightbuffer = new UniformBuffer("lighting", false, new UniformAttribs(0, "lights", 32 * 4 * 2), new UniformAttribs(32*4*2, "ambientStrength", 1));
    public boolean buffer_made;

    public Light(Vector3f pos, Vector3f color){
        this.pos = pos;
        this.color = color;
    }

    public float[] toArray(){
        return new float[]{
            pos.x,
            pos.y,
            pos.z,
            0,
            color.x,
            color.y,
            color.z,
            0
        };
    }
}