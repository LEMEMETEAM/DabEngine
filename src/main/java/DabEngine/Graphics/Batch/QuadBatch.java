package DabEngine.Graphics.Batch;

import org.lwjgl.opengl.GL11;

import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Utils.Color;

public class QuadBatch extends IBatch {

    public static Shaders DEFAULT_SHADER = new Shaders(QuadBatch.class.getResourceAsStream("/Shaders/default.vs"),
    QuadBatch.class.getResourceAsStream("/Shaders/textured.fs"));

    public QuadBatch(Shaders shader){
        super(shader);
    }

    public void addVertex(float x, float y, float u, float v, float r, float g, float b, float a){
        vertex(x, y, r, g, b, a, u, v);
    }

    public void addQuad(Texture tex, float x, float y, float width, float height, float ox, float oy, float rotation,
            Color c, float u, float v, float u2, float v2) {
        float x1,y1,x2,y2,x3,y3,x4,y4;

        checkFlush(tex);

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

        float[] tl = c.TL;
        float[] bl = c.BL;
        float[] br = c.BR;
        float[] tr = c.TR;
        
        vertex(x1, y1, tl[0], tl[1], tl[2], tl[3], u, v);
		vertex(x2, y2, bl[0], bl[1], bl[2], bl[3], u, v2);
		vertex(x3, y3, br[0], br[1], br[2], br[3], u2, v2);
		
		vertex(x3, y3, br[0], br[1], br[2], br[3], u2, v2);
		vertex(x4, y4, tr[0], tr[1], tr[2], tr[3], u2, v);
		vertex(x1, y1, tl[0], tl[1], tl[2], tl[3], u, v);
    }

    public void checkFlush(Texture t) {
        if(idx > maxsize || t != tex){
            flush();
            this.tex = t;
        }
    }
    
}