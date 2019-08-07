package DabEngine.Graphics.OpenGL;

import static org.lwjgl.opengl.GL40.*;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import DabEngine.Core.Engine;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.Models.VertexAttrib;
import DabEngine.Graphics.Models.VertexBuffer;
import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Graphics.OpenGL.Viewport.Viewport;
import DabEngine.Observer.EventManager;
public class RenderTarget {
	
	private int f_id;
	private int r_id;
	private VertexBuffer quad;
	private ArrayDeque<Shaders> fboShader = new ArrayDeque<>();
	private Texture texture;
	private Viewport vp;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public static final Shaders RENDERTARGET_SHADER_DEFAULT = new Shaders(
		RenderTarget.class.getResourceAsStream("/Shaders/defaultFBO.vs"),
		RenderTarget.class.getResourceAsStream("/Shaders/defaultFBO.fs"));
	
	public RenderTarget(Texture renderToTexture, int width, int height, Viewport vp) {
		generateFBO(renderToTexture, width, height);
		
		List<VertexAttrib> ATTRIBUTES = Arrays.asList(new VertexAttrib(0, "pos", 2), new VertexAttrib(1, "uv", 2));
		quad = new VertexBuffer(6, ATTRIBUTES);
		vertex(-1,-1,0,0);
		vertex(-1,1,0,1);
		vertex(1,1,1,1);
		vertex(1,1,1,1);
		vertex(1,-1,1,0);
		vertex(-1,-1,0,0);

		this.vp = vp;

		pushShader(RENDERTARGET_SHADER_DEFAULT);
	}

	private void generateFBO(Texture renderToTexture, int width, int height){
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
	}

	public RenderTarget(int width, int height, Viewport vp){
		this(new Texture(width, height, false, Texture.Parameters.LINEAR), width, height, vp);
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
		glViewport(0, 0, texture.getWidth(), texture.getHeight());
	}
	
	public void bindTex() {
		texture.bind(0);
	}
	
	public void unbindTex() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		vp.apply();
	}

	public void blit(){
		bindTex();
		fboShader.peek().bind();
		fboShader.peek().setUniform("texture", 0);
		draw();
		fboShader.peek().unbind();
		unbindTex();
	}

	public void pushShader(Shaders shaders){
		this.fboShader.push(shaders);
	}

	public void popShader(){
		this.fboShader.pop();
	}

	public Shaders getShader(){
		return fboShader.peek();
	}
}
