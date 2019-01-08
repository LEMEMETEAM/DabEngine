package Graphics;

import org.joml.Vector4f;

import Entities.Renderable2D;
import Graphics.Models.Texture;
import Input.InputHandler;

public class Light2D extends Renderable2D {
	
	/*
	 * Must use an empty texture
	 * http://www.1x1px.me/
	 */
	public Light2D(Texture empty_texture, float radius, Vector4f color) {
		super(radius, radius, color, true);
		// TODO Auto-generated constructor stub
		setTexture(empty_texture);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processInput(InputHandler handler) {
		// TODO Auto-generated method stub
		
	}
	
}
