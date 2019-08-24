package DabEngine.Graphics.Batch;

import java.util.Arrays;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL33.*;

import DabEngine.Core.IDisposable;
import DabEngine.Graphics.Camera;
import DabEngine.Graphics.Models.UniformAttribs;
import DabEngine.Graphics.Models.UniformBuffer;
import DabEngine.Graphics.Models.VertexAttrib;
import DabEngine.Graphics.Models.VertexBuffer;
import DabEngine.Graphics.OpenGL.Blending;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Utils.Color;
import DabEngine.Utils.Pair;

/**
 * Batch Renderer
 */
public class Batch implements IDisposable{

	public static Shaders DEFAULT_SHADER = Shaders.getUberShader("/Shaders/default.vs", "/Shaders/default.fs", new Pair<>("TEXTURED", "0"));
    protected boolean drawing;
	protected int idx;
	public static int renderCalls = 0;
	protected VertexBuffer data;
	protected int maxsize = 4096*6;
	protected Shaders shader;
	public UniformBuffer ubo;
	protected Texture[] tex_slots;
	protected static final List<VertexAttrib> ATTRIBUTES = 
								Arrays.asList(new VertexAttrib(0, "position", 3),
								new VertexAttrib(1, "color", 4),
								new VertexAttrib(2, "texCoords", 2),
								new VertexAttrib(3, "normals", 3));
	protected static final UniformAttribs[] uATTRIBUTES = new UniformAttribs[]{
		new UniformAttribs(0, "mvpMatrix", 16)
	};
	public Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f proj;
	public Blending blend;
	private boolean batched;
	public Camera cam;

	public Batch(Shaders shader, Matrix4f proj) {
		data = new VertexBuffer(maxsize, ATTRIBUTES);
		ubo = new UniformBuffer("mvp", true, uATTRIBUTES);
		this.shader = shader;
		this.proj = proj;
		ubo.bindToShader(shader);
		updateUniforms();
	}

    public void begin(boolean batched) {
		if(drawing) {
			throw new IllegalStateException("must not be drawing before calling begin()");
		}
		drawing = true;
		shader.bind();
		data.bind();
		projectionMatrix.set(proj);
		idx = 0;
		renderCalls = 0;
		tex_slots = new Texture[16];
		blend = Blending.MIX;
		blend.apply();
		this.batched = batched;
	}
	
	public void end() {
		if(!drawing) {
			throw new IllegalStateException("must be drawing before calling end()");
		}
		drawing = false;
		if(batched)
			flush();
		data.unbind();
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
		float[] arr = new float[16];
		projectionMatrix.get(arr);
		ubo.put(0, arr);
	}
	
	public void setShader(Shaders shader, boolean updateUniforms) {
		if(shader == null) {
			throw new NullPointerException("shader cannot be null; use getDefaultShader instead");
		}
		else if(this.shader == shader){
			return;
		}

		if(drawing) {
			flush();
		}

		this.shader = shader;
		ubo.bindToShader(shader);
		
		if(updateUniforms) {
			updateUniforms();
		}
		if(drawing) {
			this.shader.bind();
		}
	}
	
	public void setShader(Shaders shader) {
		setShader(shader, true);
	}
	
	public Shaders getShader() {
		return shader;
	}

	public void setBlend(Blending blend){
		if(drawing){
			flush();
		}
		this.blend = blend;
		blend.apply();
	}

	public void setTexture(Pair<Texture, Integer>... tex){
		changeTexture(tex);
	}

	/**
     * add array of vertexdata to batch 
     * Data must not be tightly packed and should be arranged in the the order of attributes per vertex.
     * Bad Example:
     *   v1  v2  v3              v1  v2
     * [xyz,xyz,xyz,xyz,xyz,xyz,rgb,rgb...]
     * Good Example:
     *   v1  v1 v1  v2  v2 v2
     * [xyz,rgb,st,xyz,rgb,st...]
     */
	public void add(float[] vData){
		for(int i = 0; i < vData.length/12; i++){
			checkSize();
			for(int j = 0; j < 12; j++){
				data.put(vData[i*12+j]);
			}
			idx++;
		}
		if(!batched){
			flush();
		}
	}

    public void addQuad(float x, float y, float z, float width, float height, float ox, float oy, float rotation,
            Color c, float u, float v, float u2, float v2) {
		checkSize();
        float x1,y1,x2,y2,x3,y3,x4,y4;

        final float cx = ox;
		final float cy = oy;
		
		final float px,py,px2,py2;
		
		px = -cx;
		py = -cy;
		px2 = width - cx;
		py2 = height - cy;
		
		if(rotation != 0) {
			
			final float cos = (float)Math.cos(Math.toRadians(rotation));
			final float sin = (float)Math.sin(Math.toRadians(rotation));
			
			x1 = x + (cos * px - sin * py) + cx;
			y1 = y + (sin * px + cos * py) + cy;
			
			x2 = x + (cos * px - sin * py2) + cx;
			y2 = y + (sin * px + cos * py2) + cy;
			
			x3 = x + (cos * px2 - sin * py2) + cx;
			y3 = y + (sin * px2 + cos * py2) + cy;
			
			x4 = x + (cos * px2 - sin * py) + cx;
			y4 = y + (sin * px2 + cos * py) + cy;
		}
		else {
			x1 = x + px + cx;
			y1 = y + py + cy;
			
			x2 = x + px + cx;
			y2 = y + py2 + cy;
			
			x3 = x + px2 + cx;
			y3 = y + py2 + cy;
			
			x4 = x + px2 + cx;
			y4 = y + py + cy;
		}
		
		float[] col = c.getColor();

		Vector3f faceNormals1 = calcNormals(x1,y1,z,x2,y2,z,x3,y3,z);
		Vector3f faceNormals2 = calcNormals(x3,y3,z,x4,y4,z,x1,y1,z);
		
		
		//x+width*y
		float[] verts = new float[]{
			x1, y1, z, col[0+4*0], col[1+4*0], col[2+4*0], col[3+4*0], u, v, faceNormals1.x, faceNormals1.y, faceNormals1.z,
			x2, y2, z, col[0+4*1], col[1+4*1], col[2+4*1], col[3+4*1], u, v2, faceNormals1.x, faceNormals1.y, faceNormals1.z,
			x3, y3, z, col[0+4*2], col[1+4*2], col[2+4*2], col[3+4*2], u2, v2, faceNormals1.x, faceNormals1.y, faceNormals1.z,
		
			x3, y3, z, col[0+4*2], col[1+4*2], col[2+4*2], col[3+4*2], u2, v2, faceNormals2.x, faceNormals2.y, faceNormals2.z,
			x4, y4, z, col[0+4*3], col[1+4*3], col[2+4*3], col[3+4*3], u2, v, faceNormals2.x, faceNormals2.y, faceNormals2.z,
			x1, y1, z, col[0+4*0], col[1+4*0], col[2+4*0], col[3+4*0], u, v, faceNormals2.x, faceNormals2.y, faceNormals2.z
		};
		add(verts);

		if(!batched){
			flush();
		}
	}
	
	private Vector3f calcNormals(float p1x, float p1y, float p1z, float p2x, float p2y, float p2z, float p3x, float p3y, float p3z){
		Vector3f U = new Vector3f(p2x - p1x, p2y - p1y, p2z - p1z);
		Vector3f V = new Vector3f(p3x - p1x, p3y - p1y, p3z - p1z);

		Vector3f N = U.cross(V, new Vector3f());
		return N.normalize();
	}

    public void changeTexture(Pair<Texture, Integer>... tex) {
		//check if atleast one texture found that arent same
		boolean one = false;
        for(var t : tex){
			if(t.left != tex_slots[t.right]){
				//flush if only one is not same
				flush();
				one = true;
				break;
			}
		}
		for(var t : tex){
			if(one){
				tex_slots[t.right] = t.left;
			}
		}
	}
	
	private void checkSize(){
		if(idx >= maxsize){
			flush();
		}
	}
    
    public void flush() {
		if(idx > 0){
			updateUniforms();
			if(cam != null){
				shader.setUniform("viewPos", cam.getPosition());
			}
			//data.flip();
			for(int i = 0; i < tex_slots.length; i++){
				Texture t = tex_slots[i];
				if(t != null){
					t.bind(i);
				}
			}
			ubo.flush();
			data.draw(GL_TRIANGLES, 0, idx);
			renderCalls++;
	
			idx = 0;
			data.clear();
		}
    }

	@Override
	public void dispose() {
		data.dispose();
		ubo.dispose();
	}
}
