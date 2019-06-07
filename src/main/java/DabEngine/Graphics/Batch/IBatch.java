package DabEngine.Graphics.Batch;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import DabEngine.Graphics.ProjectionMatrix;
import DabEngine.Graphics.Models.VertexAttrib;
import DabEngine.Graphics.Models.VertexBuffer;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.OpenGL.Textures.Texture;

public abstract class IBatch {

    protected boolean drawing;
	protected int idx;
	public static int renderCalls = 0;
	protected VertexBuffer data;
	protected int maxsize = 1000*6;
	protected Shaders shader;
	protected Texture tex;
	protected static final List<VertexAttrib> ATTRIBUTES = 
								Arrays.asList(new VertexAttrib(0, "position", 2),
								new VertexAttrib(1, "color", 4),
								new VertexAttrib(2, "texCoords", 2));

	public IBatch(Shaders shader) {
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
		tex = null;
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
		
		shader.setUniform("mvpMatrix", ProjectionMatrix.get());
		
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

    protected VertexBuffer vertex(float x, float y, float r, float g, float b, float a, float u, float v) {
        data.put(x).put(y).put(r).put(g).put(b).put(a).put(u).put(v);
        idx++;
        return data;
	}
    
    public void flush() {
		
		if(idx > 0){
			data.flip();
			if(tex != null){
				tex.bind(0);
			}
			data.bind();
			data.draw(GL11.GL_TRIANGLES, 0, idx);
			data.unbind();
			renderCalls++;
	
			idx = 0;
			data.clear();
		}
    }
}
