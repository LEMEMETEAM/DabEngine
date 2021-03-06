package DabEngine.Resources;

import static org.lwjgl.opengl.GL40.*;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;

import DabEngine.Core.App;
import DabEngine.Graphics.Viewport;
import DabEngine.Graphics.Models.VertexAttrib;
import DabEngine.Graphics.Models.VertexBuffer;
import DabEngine.Observer.EventManager;
import DabEngine.Resources.Textures.Texture;
public class RenderTarget {
	
	private int f_id;
	private int r_id;
	public Texture[] texture;
	private Viewport vp;

	public RenderTarget(int width, int height, Viewport vp, Texture... renderToTexture) {
		for(Texture t : renderToTexture) if(!t.ready) t.load();

		f_id = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, f_id);
		
		boolean MRT = renderToTexture.length > 1;
		if(MRT){
			IntBuffer buf = BufferUtils.createIntBuffer(renderToTexture.length);
			for(int i = 0; i < renderToTexture.length; i++){
				glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + i, GL_TEXTURE_2D, renderToTexture[i].getID(), 0);
				buf.put(GL_COLOR_ATTACHMENT0 + i);
			}
			buf.rewind();
			glDrawBuffers(buf);
		}
		else{
			glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, renderToTexture[0].getID(), 0);
		}
		
		r_id = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, r_id);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_STENCIL, width, height);
		glBindRenderbuffer(GL_RENDERBUFFER, 0);
		
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, r_id);
		
		if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			//TODO: add logging
			System.exit(0);
		}

		texture = renderToTexture;
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);

		this.vp = vp;
	}

	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, f_id);
		glViewport(0, 0, texture[0].getWidth(), texture[0].getHeight());
	}
	
	public void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		vp.apply();
	}
}
