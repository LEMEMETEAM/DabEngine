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

    public Mesh(float[] data, Texture[] diffuse, Texture[] specular){
        this.vData = data;
        this.diffuse = new ArrayList<>(Arrays.asList(diffuse));
        this.specular = new ArrayList<>(Arrays.asList(specular));
    }

    public void draw(Graphics g, float x, float y, float z, float scale, float rotation, Vector3f axis, Color c){
        boolean textured = false;
        for(int j = 0; j < Math.max(diffuse.size(), specular.size()); j++){
            if(j<diffuse.size()){
                g.getBatch().setTexture(new Pair<>(diffuse.get(j), 0));
            }
            if(j<specular.size()){
                g.getBatch().setTexture(new Pair<>(specular.get(j), 1));
            }
            textured = true;
        }
        if(!textured){
            g.getBatch().setTexture(new Pair<>(new Texture(0), 0));
        }
        
        g.draw(vData, x, y, z, scale, rotation, axis, c);
    }
}
