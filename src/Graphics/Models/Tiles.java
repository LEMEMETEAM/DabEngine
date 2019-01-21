package Graphics.Models;

import org.joml.Vector4f;

import Core.Engine;
import Entities.PhysicsBody;
import Input.InputHandler;

public class Tiles extends PhysicsBody {

	public Tiles(Texture texture, float x, float y, float width, float height, Vector4f color, boolean center_anchor, boolean destroyable, BodyType type) {
		super(width, height, color, center_anchor);
		setTexture(texture);
		setPosition(x, y);
		setBodyType(type);
		
		Engine.addToPhysics(this);
	}

	@Override
	public void update() {
		
	}

	@Override
	public void processInput(InputHandler handler) {
		
	}
	
	//TODO fix destroy comp
	
}