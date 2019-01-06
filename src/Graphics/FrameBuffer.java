package Graphics;

import static org.lwjgl.opengl.GL40.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import Core.Engine;
import Graphics.Models.Mesh;
public class FrameBuffer {
	
	private int f_id;
	private int t_id;
	private int r_id;
	private Mesh quad;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public FrameBuffer(Engine engine) {
		f_id = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, f_id);
		
		t_id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, f_id);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, engine.getMainWindow().getWidth(), engine.getMainWindow().getHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		glBindTexture(GL_TEXTURE_2D, 0);
		
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, t_id, 0);
		
		r_id = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, r_id);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, engine.getMainWindow().getWidth(), engine.getMainWindow().getHeight());
		glBindRenderbuffer(GL_RENDERBUFFER, 0);
		
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, r_id);
		
		if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			LOGGER.log(Level.SEVERE, "Framebuffer Incomplete");
			System.exit(0);
		}
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		
		quad = new Mesh(
				new float[] {
						-1, 1, 0,
						-1, -1, 0,
						1, -1, 0,
						1, 1, 0
				},
				new float[] {
						0, 1,
						0, 0, 
						1, 0,
						1, 1
				},
				new int[] {
						0, 1, 2,
						0, 3, 2
				});
	}
	
	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, f_id);
	}
	
	public void bindTex() {
		glBindTexture(GL_TEXTURE_2D, t_id);
	}
	
	public void unbindTex() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void renderQuad() {
		quad.render();
	}
	
	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
}
