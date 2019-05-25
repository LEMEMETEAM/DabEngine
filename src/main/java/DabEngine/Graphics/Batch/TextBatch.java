package DabEngine.Graphics.Batch;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.joml.Vector4f;

import DabEngine.Graphics.ProjectionMatrix;
import DabEngine.Graphics.Shaders;
import DabEngine.Graphics.Models.Texture;
import DabEngine.Graphics.Models.VertexAttrib;
import DabEngine.Graphics.Models.VertexBuffer;

public class TextBatch implements IBatch {
	
	private LinkedHashMap<Character, FontCharacter> Characters = new LinkedHashMap<>();
	private boolean drawing;
	private Shaders shader = new Shaders(getClass().getResourceAsStream("/resources/Shaders/default.vs"), getClass().getResourceAsStream("/resources/Shaders/textured.fs"));
	private int idx;
	public static int renderCalls;
	private int maxsize = 1000*6;
	private VertexBuffer data;
	private static final List<VertexAttrib> ATTRIBUTES = 
			Arrays.asList(new VertexAttrib(0, "position", 2),
					new VertexAttrib(1, "color", 4),
					new VertexAttrib(2, "texCoords", 2));
	private FontCharacter current_char;
	private Texture texture;
	
	private class FontCharacter{
		public Vector4f uv;
	}
	
	public TextBatch(Texture texture, Shaders shader) {
		this.shader = shader;
		this.texture = texture;
		data = new VertexBuffer(maxsize, ATTRIBUTES);
		for(int i = 32; i < 127; i++) {
			FontCharacter c = new FontCharacter();
			c.uv = texture.getRegion().getTile(i + 1);
			Characters.put((char) i, c);
		}
		updateUniforms();
	}
	
	public TextBatch(Texture texture) {
		this.texture = texture;
		data = new VertexBuffer(maxsize, ATTRIBUTES);
		for(int i = 32; i < 127; i++) {
			FontCharacter c = new FontCharacter();
			c.uv = texture.getRegion().getTile(i + 1);
			Characters.put((char) i, c);
		}
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
		current_char = null;
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
	
	public void draw(char glyph, float x, float y, float scale, float r, float g, float b, float a) {
		
		FontCharacter fc = Characters.get(glyph);
		
		checkFlush(fc);
		
		float xpos = x;
		float ypos = y;
		float width = ((fc.uv.z - fc.uv.x) * 3) * scale;
		float height = ((fc.uv.w - fc.uv.y) * 6) * scale;
		
		float x1, y1, x2, y2, x3, y3, x4, y4;
		x1 = xpos;
		y1 = ypos;
		
		x2 = xpos;
		y2 = ypos + height;
		
		x3 = xpos + width;
		y3 = ypos + height;
		
		x4 = xpos + width;
		y4 = ypos;
		
		vertex(x1, y1, r, g, b, a, fc.uv.x, fc.uv.y);
		vertex(x2, y2, r, g, b, a, fc.uv.x, fc.uv.w);
		vertex(x3, y3, r, g, b, a, fc.uv.z, fc.uv.w);
		
		vertex(x1, y1, r, g, b, a, fc.uv.x, fc.uv.y);
		vertex(x4, y4, r, g, b, a, fc.uv.z, fc.uv.y);
		vertex(x3, y3, r, g, b, a, fc.uv.z, fc.uv.w);
	}
	
	public void draw(String s, float x, float y, float scale, float r, float g, float b, float a) {
		for(int c = 0; c < s.length(); c++) {
			draw(s.charAt(c), x, y, scale, r, g, b, a);
			if(current_char != null)
				x += ((current_char.uv.z - current_char.uv.x) * 3) * scale;
		}
	}
	
	public void draw(String s, float x, float y, float scale, Vector4f rgba) {
		for(int c = 0; c < s.length(); c++) {
			draw(s.charAt(c), x, y, scale, rgba.x, rgba.y, rgba.z, rgba.w);
			if(current_char != null)
				x += ((current_char.uv.z - current_char.uv.x) * 3) * scale;
		}
	}
	
	private VertexBuffer vertex(float x, float y, float r, float g, float b, float a, float u, float v) {
		data.put(x).put(y).put(r).put(g).put(b).put(a).put(u).put(v);
		idx++;
		return data;
	}
	
	public void checkFlush(FontCharacter fc) {
		if(fc == null) {
			throw new NullPointerException("null Character");
		}
		if(fc != current_char || idx >= maxsize) {
			flush();
			current_char = fc;
		}
	}
	
	public void flush() {
		if(idx > 0) {
			data.flip();
			if(current_char != null) {
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
