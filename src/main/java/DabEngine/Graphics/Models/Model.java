package DabEngine.Graphics.Models;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL33;

import DabEngine.Graphics.Graphics;
import DabEngine.Utils.Color;
import DabEngine.Utils.Pair;

public class Model {
    public ArrayList<Mesh> meshes = new ArrayList<>();
    int vCount;

    /**
     * 3D Model
     */
    public Model(){
        
    }

    public void draw(Graphics g, Vector3f pos, Vector3f scale, Vector3f origin_rot, Vector4f rotation, Color c){
        for(Mesh m : meshes){
            m.draw(g, pos, scale, origin_rot, rotation, c);
        }
    }
}