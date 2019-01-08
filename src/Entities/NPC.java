package Entities;

import org.joml.Vector4f;

import Entities.Components.Interactable;
import Graphics.Models.Texture;
import Input.InputHandler;

public class NPC extends Renderable2D implements Interactable {
	
	@SuppressWarnings("unused")
	private String name;

	public NPC(String name, Texture texture, float width, float height, Vector4f color, boolean center_anchor) {
		super(width, height, color, center_anchor);
		// TODO Auto-generated constructor stub
		this.name = name;
		setTexture(texture);
	}

	@Override
	public void onInteraction() {
		// TODO Auto-generated method stub
		
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
