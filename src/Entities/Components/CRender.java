package Entities.Components;

import org.joml.Vector4f;

import Entities.GameObject;
import Graphics.SpriteBatch;
import Graphics.Models.Texture;

public class CRender extends Component {
	
	private Texture texture;
	private Vector4f color;
	private boolean center_anchor;
	
	public CRender() {
		name = "render";
	}
	
	public void render(SpriteBatch batch) {
		CTransform transform = this.gameObject.getComponent(CTransform.class);
		batch.draw(texture, transform.getPos().x(), transform.getPos().y(), transform.getSize().x(), transform.getSize().y(), color.x(), color.y(), color.z(), color.w(), center_anchor);
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public void setColor(Vector4f color) {
		this.color = color;
	}
	
	public void setAnchorCenter(boolean center) {
		this.center_anchor = center;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public Vector4f getColor() {
		return color;
	}
	
	public boolean isAnchorCenter() {
		return center_anchor;
	}

}
