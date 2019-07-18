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

	public QuadBatch(){
		DEFAULT_SHADER = new Shaders(QuadBatch.class.getResourceAsStream("/Shaders/default.vs"),
    QuadBatch.class.getResourceAsStream("/Shaders/textured.fs"));
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
		SpriteInfo info = new SpriteInfo();

		info.topLeft.x = x1; 
		info.topLeft.y = y1; 
		info.topLeft.z = z; 
		info.topLeft.r = col[0+4*0]; 
		info.topLeft.g = col[1+4*0]; 
		info.topLeft.b = col[2+4*0]; 
		info.topLeft.a = col[3+4*0]; 
		info.topLeft.u = u; 
		info.topLeft.v = v; 
		info.topLeft.nx = faceNormals1.x;
		info.topLeft.ny = faceNormals1.y; 
		info.topLeft.nz = faceNormals1.z;

		info.bottomLeft.x = x2; 
		info.bottomLeft.y = y2; 
		info.bottomLeft.z = z; 
		info.bottomLeft.r = col[0+4*1]; 
		info.bottomLeft.g = col[1+4*1]; 
		info.bottomLeft.b = col[2+4*1]; 
		info.bottomLeft.a = col[3+4*1]; 
		info.bottomLeft.u = u; 
		info.bottomLeft.v = v2; 
		info.bottomLeft.nx = faceNormals1.x;
		info.bottomLeft.ny = faceNormals1.y; 
		info.bottomLeft.nz = faceNormals1.z;

		info.bottomRight.x = x3; 
		info.bottomRight.y = y3; 
		info.bottomRight.z = z; 
		info.bottomRight.r = col[0+4*2]; 
		info.bottomRight.g = col[1+4*2]; 
		info.bottomRight.b = col[2+4*2]; 
		info.bottomRight.a = col[3+4*2]; 
		info.bottomRight.u = u2; 
		info.bottomRight.v = v2; 
		info.bottomRight.nx = faceNormals1.x;
		info.bottomRight.ny = faceNormals1.y; 
		info.bottomRight.nz = faceNormals1.z;

		info.topRight.x = x4; 
		info.topRight.y = y4; 
		info.topRight.z = z; 
		info.topRight.r = col[0+4*3]; 
		info.topRight.g = col[1+4*3]; 
		info.topRight.b = col[2+4*3]; 
		info.topRight.a = col[3+4*3]; 
		info.topRight.u = u2; 
		info.topRight.v = v; 
		info.topRight.nx = faceNormals2.x;
		info.topRight.ny = faceNormals2.y; 
		info.topRight.nz = faceNormals2.z;

		info.texture = tex;

		switch(sorting){
			case BACK_TO_FRONT:
				info.sortingKey = -z;
				break;
			case FRONT_TO_BACK:
				info.sortingKey = z;
				break;
			case TEXTURE:
				info.sortingKey = (float)tex.getID();
		}

		ensureCapacity();

		sprites[spriteNum++] = info;
	}
	
	private Vector3f calcNormals(float p1x, float p1y, float p1z, float p2x, float p2y, float p2z, float p3x, float p3y, float p3z){
		Vector3f U = new Vector3f(p2x - p1x, p2y - p1y, p2z - p1z);
		Vector3f V = new Vector3f(p3x - p1x, p3y - p1y, p3z - p1z);

		Vector3f N = U.cross(V, new Vector3f());
		return N.normalize();
	}

    
}