package Graphics;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTBitmap;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;

import static org.lwjgl.stb.STBTruetype.*;

import Graphics.Models.VertexAttrib;
import Graphics.Models.VertexBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Text {
	
	private STBTTFontinfo fontinfo = STBTTFontinfo.create();
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private LinkedHashMap<Character, FontCharacter> Characters = new LinkedHashMap<>();
	private boolean drawing;
	private Shaders shader;
	private int idx;
	private int renderCalls;
	private int maxsize = 1000*6;
	private VertexBuffer data;
	private static final List<VertexAttrib> ATTRIBUTES = 
			Arrays.asList(new VertexAttrib(0, "position", 2),
					new VertexAttrib(1, "color", 3),
					new VertexAttrib(2, "texCoords", 2));
	private FontCharacter current_char;
	
	private class FontCharacter{
		public int t_id;
		public Vector2f size;
		public Vector2f bearing;
		public float advance;
	}
	
	public Text(String fontname, int fontsize, Shaders shader) {
		data = new VertexBuffer(maxsize, ATTRIBUTES);
		try
		{
			ByteBuffer fontdata = Utils.Utils.ioResourceToByteBuffer(fontname, 1<<20);
			if(!stbtt_InitFont(fontinfo, fontdata)) {
				LOGGER.log(Level.SEVERE, "STB Load Failure");
			}
		
			int[] ascent = new int[1]; int baseline;
			float scale = stbtt_ScaleForPixelHeight(fontinfo, fontsize);
			stbtt_GetFontVMetrics(fontinfo, ascent, null, null);
			baseline = (int)(ascent[0]*scale);
			
			for(int c = 32; c < 128; c++) {
				int[] adv = new int[1]; int[] lsb = new int[1];
				int[] x0 = new int[1]; int[] y0 = new int[1];
				int[] x1 = new int[1]; int[] y1 = new int[1];
				
				ByteBuffer bitmap = BufferUtils.createByteBuffer(512*1024);
				
				stbtt_GetGlyphHMetrics(fontinfo, c, adv, lsb);
				stbtt_GetGlyphBitmapBoxSubpixel(fontinfo, c, scale, scale, 0, 0, x0, y0, x1, y1);
				stbtt_MakeGlyphBitmapSubpixel(fontinfo, bitmap, x1[0]-x0[0], y1[0]-y0[0], 79, scale, scale, 0, 0, c);
				
				int tex_id = glGenTextures();
				int width = x1[0] - x0[0];
				int height = y1[0] - y0[0];
				glBindTexture(GL_TEXTURE_2D, tex_id);
				glTexImage2D(
						GL_TEXTURE_2D,
						0,
						GL_RED,
						width,
						height,
						0,
						GL_RED,
						GL_UNSIGNED_BYTE,
						bitmap);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
				
				FontCharacter character = new FontCharacter() {{
					t_id = tex_id;
					size = new Vector2f(width, height);
					bearing = new Vector2f(x0[0], y0[0]);
					advance = adv[0];
				}};
				Characters.put((char) c, character);
			}
			this.shader = shader;
		} 
		catch(IOException e) 
		{
			// TODO Auto-generated catch block
			LOGGER.log(Level.SEVERE, "Font not found or font couldn't be read", e);
		}
		
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
		glActiveTexture(GL_TEXTURE0);
	}
	
	public void end() {
		if(!drawing) {
			throw new IllegalStateException("must be drawing before calling end()");
		}
		drawing = false;
		flush();
	}
	
	public void draw(char glyph, float x, float y, float scale, float r, float g, float b) {
		
		FontCharacter fc = Characters.get(glyph);
		
		checkFlush(fc);
		
		float xpos = x + fc.bearing.x() * scale;
		float ypos = y + (Characters.get('H').bearing.y() - fc.bearing.y()) * scale;
		float width = fc.size.x() * scale;
		float height = fc.size.y() * scale;
		
		float x1, y1, x2, y2, x3, y3, x4, y4;
		x1 = xpos;
		y1 = ypos;
		
		x2 = xpos;
		y2 = ypos + height;
		
		x3 = xpos + width;
		y3 = ypos + height;
		
		x4 = xpos + width;
		y4 = ypos;
		
		vertex(x1, y1, r, g, b, 0f, 0f);
		vertex(x2, y2, r, g, b, 0f, 1f);
		vertex(x3, y3, r, g, b, 1f, 1f);
		
		vertex(x1, y1, r, g, b, 0f, 0f);
		vertex(x4, y4, r, g, b, 1f, 0f);
		vertex(x3, y3, r, g, b, 1f, 1f);
	}
	
	public void draw(String s, float x, float y, float scale, float r, float g, float b) {
		for(int c = 0; c < s.length()-1; c++) {
			draw(s.charAt(c), x, y, scale, r, g, b);
			if(current_char != null)
				x += (current_char.advance) * scale;
		}
	}
	
	public void draw(String s, float x, float y, float scale, Vector3f rgb) {
		for(int c = 0; c < s.length()-1; c++) {
			draw(s.charAt(c), x, y, scale, rgb.x, rgb.y, rgb.z);
			if(current_char != null)
				x += (current_char.advance) * scale;
		}
	}
	
	private VertexBuffer vertex(float x, float y, float r, float g, float b, float u, float v) {
		data.put(x).put(y).put(r).put(g).put(b).put(u).put(v);
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
				glBindTexture(GL_TEXTURE_2D, current_char.t_id);
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
