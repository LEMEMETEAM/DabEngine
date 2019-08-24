package DabEngine.Graphics.Models;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL33;

import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Utils.Color;
import DabEngine.Utils.Pair;

public class Model {
    public ArrayList<Mesh> meshes = new ArrayList<>();
    public int bTextured;
    int vCount;

    /**
     * 3D Model
     */
    public Model(){
        
    }

    public void draw(Graphics g, float x, float y, float z, float scale, float rotation, Vector3f axis, Color c){
        for(Mesh m : meshes){
            m.draw(g, x, y, z, scale, rotation, axis, c);
        }
    }
}