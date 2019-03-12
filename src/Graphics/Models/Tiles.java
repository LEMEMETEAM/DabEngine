package Graphics.Models;

import org.joml.Vector2f;
import org.joml.Vector4f;

import Entities.Entity;
import Entities.Components.CPhysics;
import Entities.Components.CPhysics.BodyType;
import Entities.Components.CSprite;
import Entities.Components.CTransform;

public class Tiles extends Entity {

	public Tiles(Texture texture, float x, float y, float width, float height, Vector4f color, boolean center_anchor, boolean destroyable, BodyType type) {
		addComponent(new CSprite());
		addComponent(new CPhysics());
		addComponent(new CTransform());
		
		CTransform transform = this.getComponent(CTransform.class);
		transform.pos.set(x, y);
		transform.size.set(width, height);
		
		CSprite renderer = this.getComponent(CSprite.class);
		renderer.texture = texture;
		renderer.color = color;
		renderer.center_anchor = center_anchor;
		
		this.getComponent(CPhysics.class).bodytype = BodyType.STATIC;
	}
}