package Graphics.Models;

import org.joml.Vector4f;

import Entities.Objects.Object2D;
import Input.InputHandler;

public class Tiles extends Object2D {

	public Tiles(Texture texture, float x, float y, float width, float height, Vector4f color, boolean center_anchor, boolean destroyable) {
		super(width, height, color, center_anchor);
		setTexture(texture);
		setPosition(x, y);
		setDestroyable(destroyable);
	}

	@Override
	public void update() {
		
	}

	@Override
	public void processInput(InputHandler handler) {
		
	}
	
}