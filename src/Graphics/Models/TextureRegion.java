package Graphics.Models;

import org.joml.Vector4f;

public class TextureRegion {
	
	private float scalex, scaley;
	private Vector4f uv;
	
	public TextureRegion(int tileNomX, int tileNomY) {
		scalex = 1.0f / (float) tileNomX;
		scaley = 1.0f / (float) tileNomY;
		uv = new Vector4f(0, 0, scalex, scaley);
	}
	
	public TextureRegion() {
		this(1, 1);
	}
	
	public void getTile(int tileposx, int tileposy) {
		uv.x = ((tileposx - 1) * scalex);
		uv.y = (tileposy -1) * scaley;
		uv.z = uv.x + scalex;
		uv.w = uv.y + scaley;
	}
	
	public Vector4f getUV() {
		return uv;
	}
}
