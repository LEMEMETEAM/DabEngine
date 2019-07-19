package DabEngine.Graphics.Batch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import DabEngine.Graphics.Models.VertexAttrib;
import DabEngine.Graphics.Models.VertexBuffer;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.OpenGL.Textures.Texture;

public abstract class IBatch {

    protected boolean drawing;
	protected int idx;
	public static int renderCalls = 0;
	protected VertexBuffer data;
	protected int maxsize = 1000;
	protected Shaders shader;
	protected static final List<VertexAttrib> ATTRIBUTES = 
								Arrays.asList(new VertexAttrib(0, "position", 3),
								new VertexAttrib(1, "color", 4),
								new VertexAttrib(2, "texCoords", 2),
								new VertexAttrib(3, "normals", 3));
	protected VertexInfoQuad[] vInfo;
	public Matrix4f projectionMatrix = new Matrix4f();
	protected SortType sortType;
	protected int vertexArrayPosition;
	protected int bufferCount;

	public IBatch(Shaders shader, Matrix4f proj) {
		data = new VertexBuffer(maxsize*6, ATTRIBUTES);
		vInfo = new VertexInfoQuad[maxsize];
		this.shader = shader;
		projectionMatrix.set(proj);
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
		sortType = SortType.BACK_TO_FRONT;
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

	public void setProjectionMatrix(Matrix4f p){
		if(drawing)flush();
		projectionMatrix.set(p);
		if(drawing)updateUniforms();
	}
	
	public void updateUniforms(Shaders shader) {
		shader.bind();
		
		shader.setUniform("mvpMatrix", projectionMatrix);
		
		shader.setUniform("texture", 0);
	}

	public void setSort(SortType s){
		if(drawing) flush();
		sortType = s;
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

    protected void vertex(VertexInfo TL, VertexInfo BL, VertexInfo BR, VertexInfo TR, Texture tex) {
		VertexInfoQuad vi = new VertexInfoQuad(TL, BL, BR, TR);
		switch(sortType){
			case BACK_TO_FRONT:
				vi.sortKey = -TL.z;
				break;
			case FRONT_TO_BACK:
				vi.sortKey = TL.z;
				break;
		}
		vi.tex = tex;
		if(vertexArrayPosition >= vInfo.length){
			int oldSize = vInfo.length;
			int newSize = oldSize + oldSize/2; // grow by x1.5
			newSize = (newSize + 63) & (~63); // grow in chunks of 64.
			vInfo = Arrays.copyOf(vInfo, newSize);
			data = new VertexBuffer(6*(Math.min(newSize, Short.MAX_VALUE/6)), ATTRIBUTES);
		}
		vInfo[vertexArrayPosition++] = vi;
		
		idx+=6;
	}
    
    public void flush() {

		if(drawing)
			updateUniforms();
		
		if(idx > 0){
			Texture tex = null;
			for(int i = vertexArrayPosition - (idx/6); i < (vertexArrayPosition>vInfo.length?vInfo.length:vertexArrayPosition); i++){
				VertexInfoQuad bi = vInfo[i];
				if(bi != null){
					if(bi.tex != tex){
						pushToGPU(tex);

						tex = bi.tex;
					}
					data.put(bi.vtx[0].x).put(bi.vtx[0].y).put(bi.vtx[0].z).put(bi.vtx[0].r).put(bi.vtx[0].g).put(bi.vtx[0].b).put(bi.vtx[0].a).put(bi.vtx[0].u).put(bi.vtx[0].v).put(bi.vtx[0].nx).put(bi.vtx[0].ny).put(bi.vtx[0].nz);
					data.put(bi.vtx[1].x).put(bi.vtx[1].y).put(bi.vtx[1].z).put(bi.vtx[1].r).put(bi.vtx[1].g).put(bi.vtx[1].b).put(bi.vtx[1].a).put(bi.vtx[1].u).put(bi.vtx[1].v).put(bi.vtx[1].nx).put(bi.vtx[1].ny).put(bi.vtx[1].nz);
					data.put(bi.vtx[2].x).put(bi.vtx[2].y).put(bi.vtx[2].z).put(bi.vtx[2].r).put(bi.vtx[2].g).put(bi.vtx[2].b).put(bi.vtx[2].a).put(bi.vtx[2].u).put(bi.vtx[2].v).put(bi.vtx[2].nx).put(bi.vtx[2].ny).put(bi.vtx[2].nz);

					data.put(bi.vtx[3].x).put(bi.vtx[3].y).put(bi.vtx[3].z).put(bi.vtx[3].r).put(bi.vtx[3].g).put(bi.vtx[3].b).put(bi.vtx[3].a).put(bi.vtx[3].u).put(bi.vtx[3].v).put(bi.vtx[3].nx).put(bi.vtx[3].ny).put(bi.vtx[3].nz);
					data.put(bi.vtx[4].x).put(bi.vtx[4].y).put(bi.vtx[4].z).put(bi.vtx[4].r).put(bi.vtx[4].g).put(bi.vtx[4].b).put(bi.vtx[4].a).put(bi.vtx[4].u).put(bi.vtx[4].v).put(bi.vtx[4].nx).put(bi.vtx[4].ny).put(bi.vtx[4].nz);
					data.put(bi.vtx[5].x).put(bi.vtx[5].y).put(bi.vtx[5].z).put(bi.vtx[5].r).put(bi.vtx[5].g).put(bi.vtx[5].b).put(bi.vtx[5].a).put(bi.vtx[5].u).put(bi.vtx[5].v).put(bi.vtx[5].nx).put(bi.vtx[5].ny).put(bi.vtx[5].nz);
					bufferCount++;
				}
			}
			//vInfo.clear();
			pushToGPU(tex);
		}
		idx = 0;
		vertexArrayPosition = 0;
	}
	
	private void pushToGPU(Texture tex){
		if(bufferCount > 0){
			if(tex!=null){
				tex.bind(0);
			}
			data.flip();
			data.bind();
			data.draw(GL11.GL_TRIANGLES, 0, bufferCount*6);
			data.unbind();
			renderCalls++;

			bufferCount = 0;
			data.clear();
		}
	}
}

class VertexInfoQuad implements Comparable<VertexInfoQuad> {
	public VertexInfo[] vtx  = new VertexInfo[6];
	public float sortKey;
	public Texture tex;

	public VertexInfoQuad(VertexInfo TL, VertexInfo BL, VertexInfo BR, VertexInfo TR){
		vtx[0] = TL;
		vtx[1] = BL;
		vtx[2] = BR;

		vtx[3] = BR;
		vtx[4] = TR;
		vtx[5] = TL;
	}

	@Override
	public int compareTo(VertexInfoQuad obj){
		return ((Float)sortKey).compareTo(obj.sortKey);
	}
}

class VertexInfo {
	public float x, y, z, r, g, b, a, u, v, nx, ny, nz;

	public VertexInfo(float x, float y, float z, float r, float g, float b, float a, float u, float v, float nx, float ny, float nz){
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.u = u;
		this.v = v;
		this.nx = nx;
		this.ny = ny;
		this.nz = nz;
	}
}
