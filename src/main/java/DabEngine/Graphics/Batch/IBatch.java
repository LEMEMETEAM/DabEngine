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

	protected class SpriteInfo implements Comparable<SpriteInfo>{
		public Vertex topLeft = new Vertex();
		public Vertex bottomLeft = new Vertex();
		public Vertex bottomRight = new Vertex();
		public Vertex topRight = new Vertex();

		public Texture texture;
		public Float sortingKey;

		@Override
		public int compareTo(SpriteInfo o) {
			return Float.compare(sortingKey, o.sortingKey);
		}
	}
	protected Shaders currentShader;
	public static Shaders DEFAULT_SHADER;

	protected static final List<VertexAttrib> ATTRIBUTES = 
								Arrays.asList(new VertexAttrib(0, "position", 3),
								new VertexAttrib(1, "color", 4),
								new VertexAttrib(2, "texCoords", 2),
								new VertexAttrib(3, "normals", 3));
	private final int maxsize = 1000;
	protected SpriteInfo[] sprites;
	protected int spriteNum;
	protected Texture currentTexture;
	protected Matrix4f projection;
	protected SortType sorting;
	private boolean drawing;

	public IBatch(){
		sprites = new SpriteInfo[maxsize];
	}

	public void begin(SortType sorting, Shaders shader, Matrix4f projection){
		if (drawing)
			throw new IllegalStateException("must not be drawing before calling begin()");
		drawing = true;
		this.currentShader = shader;
		this.sorting = sorting;
		this.projection = projection;
		currentShader.bind();
		updateUniforms();
	}
	
	private void updateUniforms(){

		currentShader.setUniform("mvpMatrix", projection);

		currentShader.setUniform("texture", 0);
	}
	public void end(){
		if (!drawing)
			throw new IllegalStateException("must be drawing before calling end()");
		drawing =false;
		createVBO();
	}

	public void createVBO(){
		if(spriteNum == 0){
			return;
		}
		Arrays.sort(sprites, 0, spriteNum);
		VertexBuffer vbo = new VertexBuffer(sprites.length * 6, ATTRIBUTES);
		int batchIndex = 0;
		int spriteCount = spriteNum;

		while(spriteCount > 0){
			int offset = 0;
			int idx = 0;

			int batchNum =spriteCount;
			if(batchNum > maxsize){
				batchNum = maxsize;
			}
			for(int i = 0; i < batchNum; i++, batchIndex++, idx += 6){
				SpriteInfo info = sprites[batchIndex];
				if(info.texture != currentTexture){
					flush(vbo, offset, idx - offset);

					currentTexture = info.texture;
					offset = idx = 0;
				}
				vbo.put(info.topLeft.x).put(info.topLeft.y).put(info.topLeft.z).put(info.topLeft.r).put(info.topLeft.g).put(info.topLeft.b).put(info.topLeft.a).put(info.topLeft.u).put(info.topLeft.v).put(info.topLeft.nx).put(info.topLeft.ny).put(info.topLeft.nz);
				vbo.put(info.bottomLeft.x).put(info.bottomLeft.y).put(info.bottomLeft.z).put(info.bottomLeft.r).put(info.bottomLeft.g).put(info.bottomLeft.b).put(info.bottomLeft.a).put(info.bottomLeft.u).put(info.bottomLeft.v).put(info.bottomLeft.nx).put(info.bottomLeft.ny).put(info.bottomLeft.nz);
				vbo.put(info.bottomRight.x).put(info.bottomRight.y).put(info.bottomRight.z).put(info.bottomRight.r).put(info.bottomRight.g).put(info.bottomRight.b).put(info.bottomRight.a).put(info.bottomRight.u).put(info.bottomRight.v).put(info.bottomRight.nx).put(info.bottomRight.ny).put(info.bottomRight.nz);

				vbo.put(info.bottomRight.x).put(info.bottomRight.y).put(info.bottomRight.z).put(info.bottomRight.r).put(info.bottomRight.g).put(info.bottomRight.b).put(info.bottomRight.a).put(info.bottomRight.u).put(info.bottomRight.v).put(info.bottomRight.nx).put(info.bottomRight.ny).put(info.bottomRight.nz);
				vbo.put(info.topRight.x).put(info.topRight.y).put(info.topRight.z).put(info.topRight.r).put(info.topRight.g).put(info.topRight.b).put(info.topRight.a).put(info.topRight.u).put(info.topRight.v).put(info.topRight.nx).put(info.topRight.ny).put(info.topRight.nz);
				vbo.put(info.topLeft.x).put(info.topLeft.y).put(info.topLeft.z).put(info.topLeft.r).put(info.topLeft.g).put(info.topLeft.b).put(info.topLeft.a).put(info.topLeft.u).put(info.topLeft.v).put(info.topLeft.nx).put(info.topLeft.ny).put(info.topLeft.nz);
				
				info.texture = null;
			}
			flush(vbo, offset, idx - offset);
			spriteCount -= batchNum;
		}

		spriteNum = 0;
	}

	public void flush(VertexBuffer buffer, int offset, int count){
		if(count == 0){
			return;
		}
		updateUniforms();
		if(currentTexture != null){
			currentTexture.bind(0);
		}
		buffer.flip();
		buffer.bind();
		buffer.draw(GL11.GL_TRIANGLES, offset, count);
		buffer.unbind();

		buffer.clear();
	}

	public void ensureCapacity(){
		if(spriteNum >= sprites.length){
			int oldsize = sprites.length;
			int newsize = oldsize + oldsize/2;
			newsize = (newsize + 63) & (~63);
			sprites = Arrays.copyOf(sprites, newsize);
		}
	}

}

class Vertex{
	public float x, y, z, r, g, b, a, u, v, nx, ny, nz;
}
