package DabEngine.Graphics.Batch;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Utils.Color;
import java.util.Arrays;
import java.util.Collections;

public class QuadBatch extends IBatch {

    public static Shaders DEFAULT_SHADER = new Shaders(QuadBatch.class.getResourceAsStream("/Shaders/default.vs"),
    QuadBatch.class.getResourceAsStream("/Shaders/textured.fs"));

    public QuadBatch(Shaders shader, Matrix4f proj){
        super(shader, proj);
    }

    public void addQuad(Texture tex, float x, float y, float z, float width, float height, float ox, float oy, float rotation,
            Color c, float u, float v, float u2, float v2) {
        float x1,y1,x2,y2,x3,y3,x4,y4;


        final float cx = ox;
		final float cy = oy;
		
		final float px,py,px2,py2;
		
		px = -cx;
		py = -cy;
		px2 = width - cx;
		py2 = height - cy;
		
		if(rotation != 0) {
			
			final float cos = (float)Math.cos(Math.toRadians(rotation));
			final float sin = (float)Math.sin(Math.toRadians(rotation));
			
			x1 = x + (cos * px - sin * py) + cx;
			y1 = y + (sin * px + cos * py) + cy;
			
			x2 = x + (cos * px - sin * py2) + cx;
			y2 = y + (sin * px + cos * py2) + cy;
			
			x3 = x + (cos * px2 - sin * py2) + cx;
			y3 = y + (sin * px2 + cos * py2) + cy;
			
			x4 = x + (cos * px2 - sin * py) + cx;
			y4 = y + (sin * px2 + cos * py) + cy;
		}
		else {
			x1 = x + px + cx;
			y1 = y + py + cy;
			
			x2 = x + px + cx;
			y2 = y + py2 + cy;
			
			x3 = x + px2 + cx;
			y3 = y + py2 + cy;
			
			x4 = x + px2 + cx;
			y4 = y + py + cy;
		}
		
		float[] col = c.getColor();

		Vector3f faceNormals1 = calcNormals(x1,y1,z,x2,y2,z,x3,y3,z);
		Vector3f faceNormals2 = calcNormals(x3,y3,z,x4,y4,z,x1,y1,z);
		
		
		//x+width*y
        VertexInfo TL = new VertexInfo(x1, y1, z, col[0+4*0], col[1+4*0], col[2+4*0], col[3+4*0], u, v, faceNormals1.x, faceNormals1.y, faceNormals1.z);
		VertexInfo BL = new VertexInfo(x2, y2, z, col[0+4*1], col[1+4*1], col[2+4*1], col[3+4*1], u, v2, faceNormals1.x, faceNormals1.y, faceNormals1.z);
		VertexInfo BR = new VertexInfo(x3, y3, z, col[0+4*2], col[1+4*2], col[2+4*2], col[3+4*2], u2, v2, faceNormals1.x, faceNormals1.y, faceNormals1.z);
		VertexInfo TR = new VertexInfo(x4, y4, z, col[0+4*3], col[1+4*3], col[2+4*3], col[3+4*3], u2, v, faceNormals2.x, faceNormals2.y, faceNormals2.z);
		vertex(TL, BL, BR, TR, tex);

		Arrays.sort(vInfo, 0, vertexArrayPosition>vInfo.length?vInfo.length:vertexArrayPosition);

		int dab = 0;
	}
	
	private Vector3f calcNormals(float p1x, float p1y, float p1z, float p2x, float p2y, float p2z, float p3x, float p3y, float p3z){
		Vector3f U = new Vector3f(p2x - p1x, p2y - p1y, p2z - p1z);
		Vector3f V = new Vector3f(p3x - p1x, p3y - p1y, p3z - p1z);

		Vector3f N = U.cross(V, new Vector3f());
		return N.normalize();
	}

    
}