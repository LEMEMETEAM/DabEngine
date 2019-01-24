package Entities;

import org.joml.Vector4f;

import Entities.Components.CInteract;
import Entities.Components.CRender;
import Entities.Components.CTransform;
import Graphics.Models.Texture;

public class NPC extends GameObject {
	
	@SuppressWarnings("unused")
	private String name;

	public NPC(String name, Texture texture, float x, float y, float width, float height, Vector4f color, boolean center_anchor) {
		this.name = name;
		
		addComponent(new CRender());
		addComponent(new CInteract());
		addComponent(new CTransform());
		
		CRender render = this.getComponent(CRender.class);
		render.setTexture(texture);
		render.setAnchorCenter(center_anchor);
		render.setColor(color);
		
		CTransform transform = this.getComponent(CTransform.class);
		transform.setPos(x, y);
		transform.setSize(width, height);
	}
}
