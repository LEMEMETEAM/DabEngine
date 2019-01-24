package Graphics.Models;

import org.joml.Vector4f;

import Entities.GameObject;
import Entities.Components.CPhysics;
import Entities.Components.CPhysics.BodyType;
import Entities.Components.CRender;
import Entities.Components.CTransform;

public class Tiles extends GameObject {

	public Tiles(Texture texture, float x, float y, float width, float height, Vector4f color, boolean center_anchor, boolean destroyable, BodyType type) {
		addComponent(new CRender());
		addComponent(new CPhysics());
		addComponent(new CTransform());
		
		CTransform transform = this.getComponent(CTransform.class);
		transform.setPos(x, y);
		transform.setSize(width, height);
		
		CRender renderer = this.getComponent(CRender.class);
		renderer.setTexture(texture);
		renderer.setColor(color);
		renderer.setAnchorCenter(center_anchor);
		
		this.getComponent(CPhysics.class).setBodyType(BodyType.STATIC);
	}
}