package DabEngine.Graphics.OpenGL.Light;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Utils.Color;

public class Light2D {

    public Vector3f pos;
    public float radius;
    public Vector3f color;

    public static final Shaders LIGHT_SHADER = new Shaders(
        Light2D.class.getResourceAsStream("/Shaders/default.vs"),
        Light2D.class.getResourceAsStream("/Shaders/lit.fs"));

    public Light2D(Vector3f pos, float radius, Vector3f color){
        this.pos = pos;
        this.radius = radius;
        this.color = color;
    }
}