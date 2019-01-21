package Entities;

import org.joml.Vector4f;

import Graphics.Models.Texture;
import Input.InputHandler;

public class NPC extends PhysicsBody {
	
	@SuppressWarnings("unused")
	private String name;

	public NPC(String name, Texture texture, float x, float y, float width, float height, Vector4f color, boolean center_anchor) {
		super(width, height, color, center_anchor);
		// TODO Auto-generated constructor stub
		this.name = name;
		setTexture(texture);
		setPosition(x, y);
		setSolid(true);
		setBodyType(BodyType.DYNAMIC);
	}
	
	//TODO fix interact comp

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processInput(InputHandler handler) {}

}
