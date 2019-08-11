package DabEngine.Graphics.Models;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL33;

import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;

public class Model {
    ArrayList<Mesh> meshes = new ArrayList<>();
    public Matrix4f modelMatrix;
    public int textured;
    int vCount;

    /**
     * 3D Model
     */
    public Model(){
        modelMatrix = new Matrix4f();
    }

    public void draw(){
        for(int i = 0; i < meshes.size(); i++){
            for(int j = 0; j < meshes.get(i).diffuse.size(); j++){
                meshes.get(i).diffuse.get(j).bind(j % 16);
                textured = 1;
            }
            meshes.get(i).draw(GL33.GL_TRIANGLES, 0, vCount);
        }
    }
}