package Graphics.Models;

import org.joml.Vector4f;

import Entities.Renderable2D;
import Entities.Components.Destroyable;
import Input.InputHandler;

public class Tiles extends Renderable2D implements Destroyable {

	public Tiles(Texture texture, float x, float y, float width, float height, Vector4f color, boolean center_anchor, boolean destroyable) {
		super(width, height, color, center_anchor);
		setTexture(texture);
		setPosition(x, y);
	}

	@Override
	public void update() {
		
	}

	@Override
	public void processInput(InputHandler handler) {
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}
	
}