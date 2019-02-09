package Graphics.Models;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class TextureRegion {
	
	private float scalex, scaley;
	private Vector4f uv;
	private int tileNomX, tileNomY;
	
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
	
	public void setTile(int tileposx, int tileposy) {
		uv.x = ((tileposx - 1) * scalex);
		uv.y = (tileposy - 1) * scaley;
		uv.z = uv.x + scalex;
		uv.w = uv.y + scaley;
	}
	
	public void setTile(int tile) {
		uv.x = (((tile % tileNomX) - 1) * scalex);
		uv.y = (((tile / tileNomX)) * scaley);
		uv.z = uv.x + scalex;
		uv.w = uv.y + scaley;
	}
	
	public Vector4f getTile(int tileposx, int tileposy) {
		Vector4f uv = new Vector4f();
		uv.x = ((tileposx - 1) * scalex);
		uv.y = (tileposy - 1) * scaley;
		uv.z = uv.x + scalex;
		uv.w = uv.y + scaley;
		return uv;
	}
	
	public Vector4f getTile(int tile) {
		Vector4f uv = new Vector4f();
		uv.x = (((tile % tileNomX) - 1) * scalex);
		uv.y = (((tile / tileNomX)) * scaley);
		uv.z = uv.x + scalex;
		uv.w = uv.y + scaley;
		return uv;
	}
	
	public Vector4f getUV() {
		return uv;
	}
}
