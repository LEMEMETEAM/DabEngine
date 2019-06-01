package DabEngine.Graphics.Models;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Mesh {

    public Vector3f[] verts; 
    public int[] indices;
    public Vector2f[] tex;
    public Matrix4f modelMatrix;

    public Mesh(Vector3f[] verts, Vector2f[] tex, int[] indices){
        this.verts = verts;
        this.indices = indices;
        this.tex = tex;
        modelMatrix = new Matrix4f();
    }
}
