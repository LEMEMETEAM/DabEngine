package DabEngine.Graphics.Models;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL33;

import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Utils.Pair;

public class Model {
    ArrayList<Mesh> meshes = new ArrayList<>();
    public int bTextured;
    int vCount;

    /**
     * 3D Model
     */
    public Model(){
        
    }

    public void draw(Graphics g, float x, float y, float z, float scale, float rotation, Vector3f axis){
        boolean textured = false;
        for(int i = 0; i < meshes.size(); i++){
            for(int j = 0; j < meshes.get(i).diffuse.size(); j++){
                g.getBatch().setTexture(new Pair<>(meshes.get(i).diffuse.get(j), 0));
                textured = true;
            }
            for(int j = 0; j < meshes.get(i).specular.size(); j++){
                g.getBatch().setTexture(new Pair<>(meshes.get(i).specular.get(j), 1));
                textured = true;
            }
            if(!textured){
                g.getBatch().setTexture(new Pair<>(new Texture(0), 0));
            }
            
            g.draw(meshes.get(i).vData, x, y, z, scale, rotation, axis);
        }
    }
}