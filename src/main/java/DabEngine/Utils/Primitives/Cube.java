package DabEngine.Utils.Primitives;

import DabEngine.Graphics.Models.Mesh;
import DabEngine.Graphics.OpenGL.Textures.Texture;

public class Cube implements Primitive<Cube> {

    private float[] data;
    
    public Cube generate(float x, float y, float z){

        float[] data = new float[6 * 6 * 12];
        float[] points = new float[]{
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,

            0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f
        };
        float[] uvs = new float[]{
            1,0,
            1,1,
            0,1,
            0,0
        };
        int[] idx = new int[]{
            0,1,2, //front
            2,3,0,

            4,5,6, //back
            6,7,4,

            7,6,1, //left
            1,0,7,

            3,2,5, //right
            5,4,3,

            7,0,3, //top
            3,4,7,

            1,6,5, //bottom
            5,2,1
        };
        float[] normals = new float[]{
            0,0,-1,
            0,0,1,
            -1,0,0,
            1,0,0,
            0,1,0,
            0,-1,0
        };
        int[] texIdx = new int[]{
            0,1,2,2,3,0
        };

        for(int i = 0; i < 36; i++){
            data[i*12+0] = points[idx[i]*3+0] + x;
            data[i*12+1] = points[idx[i]*3+1] + y;
            data[i*12+2] = points[idx[i]*3+2] + z;

            data[i*12+3] = 1;
            data[i*12+4] = 1;
            data[i*12+5] = 1;
            data[i*12+6] = 1;

            data[i*12+7] = uvs[texIdx[i%6]*2+0];
            data[i*12+8] = uvs[texIdx[i%6]*2+1];

            data[i*12+9] = normals[(i/6)*3+0];
            data[i*12+10] = normals[(i/6)*3+1];
            data[i*12+11] = normals[(i/6)*3+2];
        }
        this.data = data;
        return this;
    }

    public Mesh toMesh(){
        Mesh m = new Mesh(data, new Texture[1], new Texture[1], new Texture[1]);
        return m;
    }
}