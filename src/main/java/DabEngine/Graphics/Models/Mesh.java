package DabEngine.Graphics.Models;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.OpenGL.Textures.Texture;
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

    public void draw(Graphics g, float x, float y, float z, Vector3f scale, float rotation, Vector3f axis, Color c){
        boolean textured = false;
        for(int j = 0; j < Math.max(normal.size(), Math.max(diffuse.size(), specular.size())); j++){
            if(j<diffuse.size()){
                g.getBatch().setTexture(new Pair<>(diffuse.get(j), 0));
            }
            if(j<specular.size()){
                g.getBatch().setTexture(new Pair<>(specular.get(j), 1));
            }
            if(j<normal.size()){
                g.getBatch().setTexture(new Pair<>(normal.get(j), 2));
            }
            textured = true;
        }
        if(!textured){
            g.getBatch().setTexture(new Pair<>(new Texture(0), 0));
        }
        
        g.draw(vData, x, y, z, scale, rotation, axis, c);
    }
}
