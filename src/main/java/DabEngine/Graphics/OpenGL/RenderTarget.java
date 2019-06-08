package DabEngine.Graphics.OpenGL;

import static org.lwjgl.opengl.GL40.*;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import DabEngine.Core.Engine;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.Models.VertexAttrib;
import DabEngine.Graphics.Models.VertexBuffer;
import DabEngine.Graphics.OpenGL.Textures.Texture;
public class RenderTarget {
	
	private int f_id;
	private int r_id;
	private VertexBuffer quad;
	private Shaders fboShader;
	private Texture texture;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public RenderTarget(Texture renderToTexture, int width, int height) {
		f_id = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, f_id);
		
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, renderToTexture.getID(), 0);
		
		r_id = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, r_id);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height);
		glBindRenderbuffer(GL_RENDERBUFFER, 0);
		
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, r_id);
		
		if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			LOGGER.log(Level.SEVERE, "Framebuffer Incomplete");
			System.exit(0);
		}

		texture = renderToTexture;
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		
		List<VertexAttrib> ATTRIBUTES = Arrays.asList(new VertexAttrib(0, "pos", 2), new VertexAttrib(1, "uv", 2));
		quad = new VertexBuffer(6, ATTRIBUTES);
		vertex(-1,-1,0,0);
		vertex(-1,1,0,1);
		vertex(1,1,1,1);
		vertex(1,1,1,1);
		vertex(1,-1,1,0);
		vertex(-1,-1,0,0);
	}

	public RenderTarget(int width, int height){
		this(new Texture(width, height, false, Texture.Parameters.LINEAR), width, height);
	}
	
	private void vertex(float x, float y, float u, float v) {
		quad.put(x).put(y).put(u).put(v);
	}
	
	public void draw() {
		quad.flip();
		quad.bind();
		quad.draw(GL_TRIANGLES, 0, 6);
		quad.unbind();
	}
	
	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, f_id);
	}
	
	public void bindTex() {
		glBindTexture(GL_TEXTURE_2D, texture.getID());
	}
	
	public void unbindTex() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public void blit(){
		bindTex();
		fboShader.bind();
		draw();
		fboShader.unbind();
		unbindTex();
	}

	public void setShader(Shaders shaders){
		this.fboShader = shaders;
	}

	public Shaders getShader(){
		return fboShader;
	}
}
