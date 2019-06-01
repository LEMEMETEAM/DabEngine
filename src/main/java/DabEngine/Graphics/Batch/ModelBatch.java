package DabEngine.Graphics.Batch;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.util.Arrays;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import DabEngine.Graphics.ProjectionMatrix;
import DabEngine.Graphics.Shaders;
import DabEngine.Graphics.Models.Mesh;
import DabEngine.Graphics.Models.Texture;
import DabEngine.Graphics.Models.VertexAttrib;
import DabEngine.Graphics.Models.VertexBuffer;

public class ModelBatch implements IBatch {
	
	private boolean drawing;
	private int idx;
	public static int renderCalls = 0;
	private Texture texture;
	private Mesh mesh;
	private VertexBuffer data;
	private int maxsize = 1000*6;
	private Shaders shader = new Shaders(getClass().getResourceAsStream("/Shaders/default3D.vs"), getClass().getResourceAsStream("/Shaders/textured.fs"));
	private static final List<VertexAttrib> ATTRIBUTES = 
			Arrays.asList(new VertexAttrib(0, "position", 3),
			new VertexAttrib(1, "color", 4),
			new VertexAttrib(2, "texCoords", 2));
	
	public ModelBatch(Shaders shader) {
		data = new VertexBuffer(maxsize, ATTRIBUTES);
		this.shader = shader;
		updateUniforms();
	}
	
	public ModelBatch() {
		data = new VertexBuffer(maxsize, ATTRIBUTES);
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
		texture = null;
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
	
	public Texture getTexture() {
		return texture;
	}
	
	public void draw(Mesh mesh, Texture tex, Vector3f pos, Vector3f size, Vector4f color) {
		checkFlush(tex, mesh);
		Vector3f[] verts = mesh.verts;
		Vector2f[] uv = mesh.tex;
		
		for(int index : mesh.indices) {
			vertex(verts[index].x, verts[index].y, verts[index].z, color.x, color.y, color.z, color.w, uv[index].x, uv[index].y);
		}
		
		mesh.modelMatrix.identity();
		shader.setUniform("modelMatrix", mesh.modelMatrix.translate(pos).scale(size));
	}
	
	private VertexBuffer vertex(float x, float y, float z, float r, float g, float b, float a, float u, float v) {
		data.put(x).put(y).put(z).put(r).put(g).put(b).put(a).put(u).put(v);
		idx++;
		return data;
	}
	
	public void checkFlush(Texture tex, Mesh mesh) {
		if(tex == null) {
			throw new NullPointerException("null texture");
		}
		if(tex != this.texture || mesh != this.mesh || idx >= maxsize) {
			flush();
			this.texture = tex;
			this.mesh = mesh;
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
