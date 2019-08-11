package DabEngine.Graphics.Models;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import DabEngine.Graphics.OpenGL.Textures.Texture;

import static org.lwjgl.opengl.GL33.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Mesh {

    public VertexBuffer vbo;
    public VertexAttrib[] attribs;
    public ArrayList<Texture> diffuse;
    /** unused */
    private ArrayList<Texture> specular;

    public Mesh(VertexBuffer vbo, Texture[] diffuse, Texture[] specular){
        this.vbo = vbo;
        this.attribs = vbo.attribs;
        this.diffuse = new ArrayList<>(Arrays.asList(diffuse));
        this.specular = new ArrayList<>(Arrays.asList(specular));
    }

    
    public Mesh(VertexAttrib... attribs){
        this.attribs = attribs;
    }

    /**
     * Create a Mesh from float array. 
     * Data must not be tightly packed and should be arranged in the the order of attributes per vertex.
     * Bad Example:
     *   v1  v2  v3              v1  v2
     * [xyz,xyz,xyz,xyz,xyz,xyz,rgb,rgb...]
     * Good Example:
     *   v1  v1 v1  v2  v2 v2
     * [xyz,rgb,st,xyz,rgb,st...]
     */
    public void build(float[] data, Texture[] diffuse, Texture[] specular){
        int total = 0;
        for(var attrib:attribs){
            total += attrib.numComponents;
        }

        this.vbo = new VertexBuffer(data.length / total, attribs);
        for(float f:data){
            vbo.put(f);
        }

        this.diffuse = new ArrayList<>(Arrays.asList(diffuse));
        this.specular = new ArrayList<>(Arrays.asList(specular));
    }

    public void draw(int type, int offset, int count){
        vbo.bind();
        vbo.draw(type, offset, count);
        vbo.unbind();
    }
}
