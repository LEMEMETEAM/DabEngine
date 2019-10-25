package DabEngine.Graphics.Models;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import DabEngine.Graphics.Graphics;
import DabEngine.Resources.Textures.*;
import DabEngine.Utils.Color;
import DabEngine.Utils.Pair;

import static org.lwjgl.opengl.GL33.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Mesh {

    public float[] vData;
    public ArrayList<Texture> diffuse;
    public ArrayList<Texture> specular;
    public ArrayList<Texture> normal;
    public String name;

    public Mesh(float[] data, Texture[] diffuse, Texture[] specular, Texture[] normals){
        this.vData = data;
        this.diffuse = new ArrayList<>(Arrays.asList(diffuse));
        this.specular = new ArrayList<>(Arrays.asList(specular));
        this.normal = new ArrayList<>(Arrays.asList(normals));
    }

    public void draw(Graphics g, Vector3f pos, Vector3f scale, Vector3f origin_rot, Vector4f rotation, Color c){
        boolean textured = false;
        for(int j = 0; j < Math.max(normal.size(), Math.max(diffuse.size(), specular.size())); j++){
            if(j<diffuse.size()){
                g.setTexture(0, diffuse.get(j));
            }
            if(j<specular.size()){
                g.setTexture(1, specular.get(j));
            }
            if(j<normal.size()){
                g.setTexture(2, normal.get(j));
            }
            textured = true;
        }
        if(!textured){
            g.setTexture(0, null);
        }
        
        g.draw(vData, pos, scale, origin_rot, rotation, c);
    }
}
