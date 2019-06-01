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
import org.lwjgl.system.MemoryStack;

import DabEngine.Graphics.ProjectionMatrix;
import DabEngine.Graphics.Shaders;
import DabEngine.Graphics.Models.Texture;
import DabEngine.Graphics.Models.VertexAttrib;
import DabEngine.Graphics.Models.VertexBuffer;
import DabEngine.Utils.Utils;

import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.opengl.GL11.*;

public class TextBatch implements IBatch {

	private LinkedHashMap<Character, FontCharacter> Characters = new LinkedHashMap<>();
	private boolean drawing;
	private Shaders shader = new Shaders(getClass().getResourceAsStream("/Shaders/textDefault.vs"),
			getClass().getResourceAsStream("/Shaders/text.fs"));
	private int idx;
	public static int renderCalls;
	private int maxsize = 1000 * 6;
	private VertexBuffer data;
	private static final List<VertexAttrib> ATTRIBUTES = Arrays.asList(new VertexAttrib(0, "position", 2),
			new VertexAttrib(1, "color", 4), new VertexAttrib(2, "texCoords", 2));
	private float scaleFactor;
	private STBTTBakedChar.Buffer cData;
	private int texID;
	private STBTTFontinfo info;

	private class FontCharacter {
		public Vector4f uv;
	}

	public TextBatch(String fontFile) {
		data = new VertexBuffer(maxsize, ATTRIBUTES);

		ByteBuffer font = null;
		try {
			font = Utils.ioResourceToByteBuffer(fontFile, 512 * 1024);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		info = STBTTFontinfo.create();
		if((!stbtt_InitFont(info, font))){
			throw new IllegalStateException("Failed to initialize font information.");
		}

		texID = glGenTextures();

		cData = STBTTBakedChar.malloc(6 * 128);
		scaleFactor = stbtt_ScaleForPixelHeight(info, 24);
		ByteBuffer bitmap = BufferUtils.createByteBuffer(512 * 512);
		stbtt_BakeFontBitmap(font, 24, bitmap, 512, 512, 32, cData);
		glBindTexture(GL_TEXTURE_2D, texID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, 512, 512, 0, GL_RED, GL_UNSIGNED_BYTE, bitmap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glBindTexture(GL_TEXTURE_2D, texID);

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

	public void setFont(String fontFile){
		ByteBuffer font = null;
		try {
			font = Utils.ioResourceToByteBuffer(fontFile, 512 * 1024);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		info = STBTTFontinfo.create();
		if((!stbtt_InitFont(info, font))){
			throw new IllegalStateException("Failed to initialize font information.");
		}

		cData = STBTTBakedChar.malloc(6 * 128);
		scaleFactor = stbtt_ScaleForPixelHeight(info, 24);
		ByteBuffer bitmap = BufferUtils.createByteBuffer(512 * 512);
		stbtt_BakeFontBitmap(font, 24, bitmap, 512, 512, 32, cData);
		glBindTexture(GL_TEXTURE_2D, texID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, 512, 512, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	}

	private float scale(float center, float offset, float factor) {
        return (offset - center) * factor + center;
	}
	
	private int getCP(String text, int to, int i, IntBuffer cpOut) {
        char c1 = text.charAt(i);
        if (Character.isHighSurrogate(c1) && i + 1 < to) {
            char c2 = text.charAt(i + 1);
            if (Character.isLowSurrogate(c2)) {
                cpOut.put(0, Character.toCodePoint(c1, c2));
                return 2;
            }
        }
        cpOut.put(0, c1);
        return 1;
    }
	
	
	public void draw(String s, float x, float y, float scale, float r, float g, float b, float a) {
		try(MemoryStack stack = stackPush()){
			FloatBuffer pX = stack.mallocFloat(1);
			FloatBuffer pY = stack.mallocFloat(1);
			IntBuffer pCodePoint = stack.mallocInt(1);

			STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);

			float pScale = stbtt_ScaleForPixelHeight(info, scale);

			pX.put(0, x);
			pY.put(0, y);


			for(int c = 0, to = s.length(); c < to; c++) {

				getCP(s, to, c, pCodePoint);

				int cp = pCodePoint.get(0);
		
				checkFlush(cp);
			
				stbtt_GetBakedQuad(cData, 512, 512, cp - 32, pX, pY, q, true);

				float cpX = pX.get(0);
				pX.put(0, scale(cpX, pX.get(0), 1));
				pX.put(0, pX.get(0) + stbtt_GetCodepointKernAdvance(info, cp, pCodePoint.get(0)) * pScale);

				float x0, y0, x1, y1;

				x0 = scale(cpX, q.x0(), 1);
				y0 = scale(0, q.y0(), 1);
				x1 = scale(cpX, q.x1(), 1);
				y1 = scale(0, q.y1(), 1);

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
	}
	
	private VertexBuffer vertex(float x, float y, float r, float g, float b, float a, float u, float v) {
		data.put(x).put(y).put(r).put(g).put(b).put(a).put(u).put(v);
		idx++;
		return data;
	}
	
	public void checkFlush(int cp) {
		if(cp == 0) {
			throw new NullPointerException("null Character");
		}
		if(cp == 32 || idx >= maxsize) {
			flush();
		}
	}
	
	public void flush() {
		if(idx > 0) {
			data.flip();
			glBindTexture(GL_TEXTURE_2D, texID);
			data.bind();
			data.draw(GL_TRIANGLES, 0, idx);
			data.unbind();
			renderCalls++;
			
			idx = 0;
			data.clear();
		}
	}
}
