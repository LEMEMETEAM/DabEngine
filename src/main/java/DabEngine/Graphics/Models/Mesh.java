package DabEngine.Graphics.Models;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import DabEngine.Graphics.OpenGL.Textures.Texture;

import static org.lwjgl.opengl.GL33.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Mesh {

    public float[] vData;
    public VertexAttrib[] attribs;
    public ArrayList<Texture> diffuse;
    /** unused */
    private ArrayList<Texture> specular;

    public Mesh(float[] data, Texture[] diffuse, Texture[] specular){
        this.vData = data;
        this.diffuse = new ArrayList<>(Arrays.asList(diffuse));
        this.specular = new ArrayList<>(Arrays.asList(specular));
    }

    
}
