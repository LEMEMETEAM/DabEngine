package Graphics;

import static org.lwjgl.opengl.GL11.*;

import java.util.Arrays;
import java.util.List;

import org.joml.Vector4f;

import Graphics.Models.Texture;
import Graphics.Models.VertexAttrib;
import Graphics.Models.VertexBuffer;

public class SpriteBatch {
	
	private boolean drawing;
	private int idx;
	public static int renderCalls = 0;
	private Texture texture;
	private VertexBuffer data;
	private int maxsize = 1000*6;
	private Shaders shader;
	private static final List<VertexAttrib> ATTRIBUTES = 
			Arrays.asList(new VertexAttrib(0, "position", 2),
			new VertexAttrib(1, "color", 4),
			new VertexAttrib(2, "texCoords", 2));
	
	public SpriteBatch(Shaders shader) {
		data = new VertexBuffer(maxsize, ATTRIBUTES);
		this.shader = shader;
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
			shader.bind();
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
	
	public void updateUniforms(Shaders shaders) {
		shader.setUniform("texture_sampler", 0);
	}
	
	public void updateUniforms() {
		updateUniforms(shader);
	}
	
	//draws a texture with no tint, full opacity and texture wrapped fully
	public void draw(Texture tex, float x, float y, float width, float height) {
		draw(tex, x, y, width, height, 0, 0, 1, 1, 1, 1, 1, 1, true);
	}
	
	public void draw(Texture tex, float x, float y, float width, float height, boolean center_anchor) {
		draw(tex, x, y, width, height, 0, 0, 1, 1, 1, 1, 1, 1, center_anchor);
	}
	
	//simplified version of above
	public void draw(Texture tex, Vector4f xywh) {
		draw(tex, xywh.x(), xywh.y(), xywh.z(), xywh.w(), 0, 0, 1, 1, 1, 1, 1, 1, true);
	}
	
	public void draw(Texture tex, Vector4f xywh, boolean center_anchor) {
		draw(tex, xywh.x(), xywh.y(), xywh.z(), xywh.w(), 0, 0, 1, 1, 1, 1, 1, 1, center_anchor);
	}
	
	//draw a texture with full texture wrap
	public void draw(Texture tex, float x, float y, float width, float height, float r, float g, float b, float a) {
		draw(tex, x, y, width, height, 0, 0, 1, 1, r, g, b, a, true);
	}
	
	public void draw(Texture tex, float x, float y, float width, float height, float r, float g, float b, float a, boolean center_anchor) {
		draw(tex, x, y, width, height, 0, 0, 1, 1, r, g, b, a, center_anchor);
	}
	
	//simplified version of above
	public void draw(Texture tex, Vector4f xywh, Vector4f rgba) {
		draw(tex,  xywh.x(), xywh.y(), xywh.z(), xywh.w(), 0, 0, 1, 1, rgba.x(), rgba.y(), rgba.z(), rgba.w(), true);
	}
	
	public void draw(Texture tex, Vector4f xywh, Vector4f rgba, boolean center_anchor) {
		draw(tex,  xywh.x(), xywh.y(), xywh.z(), xywh.w(), 0, 0, 1, 1, rgba.x(), rgba.y(), rgba.z(), rgba.w(), center_anchor);
	}
	
	public void draw(Texture tex, Vector4f xywh, Vector4f uv, Vector4f rgba) {
		draw(tex,  xywh.x(), xywh.y(), xywh.z(), xywh.w(), uv.x(), uv.y(), uv.z(), uv.w(), rgba.x(), rgba.y(), rgba.z(), rgba.w(), true);
	}
	
	public void draw(Texture tex, Vector4f xywh, Vector4f uv, Vector4f rgba, boolean center_anchor) {
		draw(tex,  xywh.x(), xywh.y(), xywh.z(), xywh.w(), uv.x(), uv.y(), uv.z(), uv.w(), rgba.x(), rgba.y(), rgba.z(), rgba.w(), center_anchor);
	}
	
	//full draw method
	public void draw(Texture tex, float x, float y, float width, float height, float u, float v, float u2, float v2, float r, float g, float b, float a, boolean center_anchor) {
		float x1,y1,x2,y2,x3,y3,x4,y4;
		
		checkFlush(tex);
		
		if(!center_anchor) {
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
		}
		
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