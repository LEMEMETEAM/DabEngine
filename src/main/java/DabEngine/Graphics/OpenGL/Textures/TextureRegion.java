package DabEngine.Graphics.OpenGL.Textures;

import org.joml.Vector4f;

public class TextureRegion {
	
	private float scalex, scaley;
	private Vector4f uv;
	public int tileNomX, tileNomY;
	
	public TextureRegion(int tileNomX, int tileNomY) {
		scalex = 1.0f / (float) tileNomX;
		scaley = 1.0f / (float) tileNomY;
		uv = new Vector4f(0, 0, scalex, scaley);
		
		this.tileNomX = tileNomX;
		this.tileNomY = tileNomY;
	}
	
	public TextureRegion() {
		this(1, 1);
	}
	
	public TextureRegion setTile(int tileposx, int tileposy) {
		uv.x = ((tileposx - 1) * scalex);
		uv.y = (tileposy - 1) * scaley;
		uv.z = uv.x + scalex;
		uv.w = uv.y + scaley;
		return this;
	}

	public TextureRegion setTile(int tilexmin, int tilexmax, int tileymin, int tileymax){
		uv.x = ((tilexmin - 1) * scalex);
		uv.y = ((tileymin - 1) * scaley);
		uv.z = ((tilexmax - 1) * scalex) + scalex;
		uv.w = ((tileymax - 1) * scaley) + scaley;
		return this;
	}
	
	public TextureRegion setTile(int tile) {
		uv.x = (((tile - 1 % tileNomX)) * scalex);
		uv.y = (((tile / tileNomX)) * scaley);
		uv.z = uv.x + scalex;
		uv.w = uv.y + scaley;
		return this;
	}

	public TextureRegion setUV(float u, float v, float s, float t){
		uv.set(u, v, s, t);
		return this;
	}
	
	public Vector4f getUV() {
		return uv;
	}
}
