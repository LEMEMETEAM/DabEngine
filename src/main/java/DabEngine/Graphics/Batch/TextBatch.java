package DabEngine.Graphics.Batch;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.io.*;
import java.nio.*;

import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.assimp.AIMeshAnim.Buffer;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.MemoryStack;

import DabEngine.Graphics.ProjectionMatrix;
import DabEngine.Graphics.Shaders;
import DabEngine.Graphics.Models.Texture;
import DabEngine.Graphics.Models.VertexAttrib;
import DabEngine.Graphics.Models.VertexBuffer;
import DabEngine.Utils.Utils;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryStack.*;

public class TextBatch implements IBatch {

	private boolean drawing;
	private Shaders shader = new Shaders(getClass().getResourceAsStream("/Shaders/textDefault.vs"),
			getClass().getResourceAsStream("/Shaders/text.fs"));
	private int idx;
	public static int renderCalls;
	private int maxsize = 1000 * 6;
	private VertexBuffer data;
	private static final List<VertexAttrib> ATTRIBUTES = Arrays.asList(new VertexAttrib(0, "position", 2),
			new VertexAttrib(1, "color", 4), new VertexAttrib(2, "texCoords", 2));
	private Font font;

	public TextBatch() {
		data = new VertexBuffer(maxsize, ATTRIBUTES);

		updateUniforms();
	}

	public TextBatch(Shaders shader){
		data = new VertexBuffer(maxsize, ATTRIBUTES);
		this.shader = shader;
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

	private float scale(float center, float offset, float factor) {
        return (offset - center) * factor + center;
    }
	
	/**
	 * <P>draw for single char
	 * there is no scaling (will be fixed soon).</p>
	 */
	public void draw(Font f, char s, FloatBuffer x, FloatBuffer y, float r, float g, float b, float a) {
		checkFlush(f, s);
		try(MemoryStack stack = stackPush()){

			STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);

			f.getData().position(0);
			
			stbtt_GetPackedQuad(f.getData(), 512, 512, s, x, y, q, f.integer_align);

			float x0, y0, x1, y1;
			x0 = q.x0();
			y0 = q.y0();
			x1 = q.x1();
			y1 = q.y1();

			/**
			 * 0,1,2
			 * 2,3,0
			 */
			vertex(x0, y0, r, g, b, a, q.s0(), q.t0());
			vertex(x0, y1, r, g, b, a, q.s0(), q.t1());
			vertex(x1, y1, r, g, b, a, q.s1(), q.t1());

			vertex(x1, y1, r, g, b, a, q.s1(), q.t1());
			vertex(x1, y0, r, g, b, a, q.s1(), q.t0());
			vertex(x0, y0, r, g, b, a, q.s0(), q.t0());
		}
	}

	public void draw(Font f, String s, float x, float y, float r, float g, float b, float a){
		try(MemoryStack stack = stackPush()){
			FloatBuffer pX = stack.floats(x);
			FloatBuffer pY = stack.floats(y);

			for(int c = 0; c < s.length(); c++){
				draw(f, s.charAt(c), pX, pY, r, g, b, a);
			}
		}
	}
	
	private VertexBuffer vertex(float x, float y, float r, float g, float b, float a, float u, float v) {
		data.put(x).put(y).put(r).put(g).put(b).put(a).put(u).put(v);
		idx++;
		return data;
	}
	
	public void checkFlush(Font f, int cp) {
		if(cp == 0) {
			throw new NullPointerException("null Character");
		}
		if(cp == 32 || idx >= maxsize) {
			flush();
			return;
		}
		else if(f != font){
			flush();
			font = f;
			return;
		}
	}
	
	public void flush() {
		if(idx > 0) {
			data.flip();
			glBindTexture(GL_TEXTURE_2D, font.getTexture());
			data.bind();
			data.draw(GL_TRIANGLES, 0, idx);
			data.unbind();
			renderCalls++;
			
			idx = 0;
			data.clear();
		}
	}
}
