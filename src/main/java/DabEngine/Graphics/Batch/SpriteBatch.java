package DabEngine.Graphics.Batch;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.util.Arrays;
import java.util.List;

import org.joml.Vector4f;

import DabEngine.Graphics.ProjectionMatrix;
import DabEngine.Graphics.Shaders;
import DabEngine.Graphics.Models.Texture;
import DabEngine.Graphics.Models.VertexAttrib;
import DabEngine.Graphics.Models.VertexBuffer;

/**
 * SpriteBatch used for batching many vertices so that they can all be sent to the GPU at the same time.
 */

public class SpriteBatch implements IBatch {
	
	private boolean drawing;
	private int idx;
	public static int renderCalls = 0;
	private Texture texture;
	private VertexBuffer data;
	private int maxsize = 1000*6;
	public final Shaders defShader = new Shaders(getClass().getResourceAsStream("/Shaders/default.vs"), getClass().getResourceAsStream("/Shaders/textured.fs"));
	private Shaders shader;
	private static final List<VertexAttrib> ATTRIBUTES = 
			Arrays.asList(new VertexAttrib(0, "position", 2),
			new VertexAttrib(1, "color", 4),
			new VertexAttrib(2, "texCoords", 2));
	
	public SpriteBatch(Shaders shader) {
		data = new VertexBuffer(maxsize, ATTRIBUTES);
		this.shader = shader;
		updateUniforms();
	}
	
	public SpriteBatch() {
		data = new VertexBuffer(maxsize, ATTRIBUTES);
		this.shader = defShader;
		updateUniforms();
	}
	
	public void begin() {
		if(drawing) {
			throw new IllegalStateException("must not be drawing before calling begin()");
		}
		drawing = true;
		shader.bind();
		idx = 0;
		renderCalls = 0;
		texture = null;
	}
	
	public void end() {
		if(!drawing) {
			throw new IllegalStateException("must be drawing before calling end()");
		}
		drawing = false;
		flush();
	}
	
	public void updateUniforms() {
		updateUniforms(shader);
	}
	
	public void updateUniforms(Shaders shader) {
		shader.bind();
		
		shader.setUniform("projectionMatrix", ProjectionMatrix.get());
		
		shader.setUniform("texture", 0);
	}
	
	public void setShader(Shaders shader, boolean updateUniforms) {
		if(shader == null) {
			throw new NullPointerException("shader cannot be null; use getDefaultShader instead");
		}
		if(drawing) {
			flush();
		}
		this.shader = shader;
		
		if(updateUniforms) {
			updateUniforms();
		}
		else if(drawing) {
			this.shader.bind();
		}
	}
	
	public void setShader(Shaders shader) {
		setShader(shader, true);
	}
	
	public Shaders getShader() {
		return shader;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	//draws a texture with no tint, full opacity and texture wrapped fully
	public void draw(Texture tex, float x, float y, float width, float height) {
		draw(tex, x, y, width, height, 0, x, y, 1, 1, 1, 1);
	}
	
	public void draw(Texture tex, float x, float y, float width, float height, float rotation) {
		draw(tex, x, y, width, height, rotation, x, y, 1, 1, 1, 1);
	}
	
	public void draw(Texture tex, float x, float y, float width, float height, Vector4f color) {
		draw(tex, x, y, width, height, 0, x, y, color.x, color.y, color.z, color.w);
	}
	
	public void draw(Texture tex, float x, float y, float width, float height, float rotation, Vector4f color) {
		draw(tex, x, y, width, height, rotation, x, y, color.x, color.y, color.z, color.w);
	}
	
	public void draw(Texture tex, float x, float y, float width, float height, float ox, float oy) {
		draw(tex, x, y, width, height, 0, ox, oy, 1, 1, 1, 1);
	}
	
	public void draw(Texture tex, float x, float y, float width, float height, float ox, float oy, float rotation) {
		draw(tex, x, y, width, height, rotation, ox, oy, 1, 1, 1, 1);
	}
	
	//full draw method
	//rotation in degrees
	public void draw(Texture tex, float x, float y, float width, float height, float rotation, float originX, float originY, float r, float g, float b, float a) {
		float x1,y1,x2,y2,x3,y3,x4,y4;
		
		checkFlush(tex);
		
		final float cx = originX;
		final float cy = originY;
		
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
		/*if(!center_anchor) {
			x1 = x;
			y1 = y;
			
			x2 = x;
			y2 = y + height;
			
			x3 = x + width;
			y3 = y + height;
			
			x4 = x + width;
			y4 = y;
		}
		else{
			x1 = x - (width/2f);
			y1 = y - (height/2f);
			
			x2 = x - (width/2f);
			y2 = y + (height/2f);
			
			x3 = x + (width/2f);
			y3 = y + (height/2f);
			
			x4 = x + (width/2f);
			y4 = y - (height/2f);
		}*/
		
		Vector4f uv = tex.getRegion().getUV();
		float u = uv.x(), v = uv.y(), u2 = uv.z, v2 = uv.w;
		
		//0, 1, 2
		//0, 3, 2
		vertex(x1, y1, r, g, b, a, u, v);
		vertex(x2, y2, r, g, b, a, u, v2);
		vertex(x3, y3, r, g, b, a, u2, v2);
		
		vertex(x1, y1, r, g, b, a, u, v);
		vertex(x4, y4, r, g, b, a, u2, v);
		vertex(x3, y3, r, g, b, a, u2, v2);
	}
	
	private VertexBuffer vertex(float x, float y, float r, float g, float b, float a, float u, float v) {
		data.put(x).put(y).put(r).put(g).put(b).put(a).put(u).put(v);
		idx++;
		return data;
	}
	
	public void checkFlush(Texture tex) {
		if(tex == null) {
			throw new NullPointerException("null texture");
		}
		if(tex != this.texture || idx >= maxsize) {
			flush();
			this.texture = tex;
		}
	}
	
	public void flush() {
		if(idx > 0) {
			data.flip();
			if(texture != null) {
				texture.bind();
			}
			data.bind();
			data.draw(GL_TRIANGLES, 0, idx);
			data.unbind();
			renderCalls++;
			
			idx = 0;
			data.clear();
		}
	}
}