package Entities.Components;

import org.joml.Vector2f;

public class CTransform extends Component {
	
	private Vector2f pos;
	private Vector2f size;
	
	public CTransform() {
		
		pos = new Vector2f();
		size = new Vector2f();
	}
	
	public Vector2f getPos() {
		return pos;
	}
	
	public Vector2f getSize() {
		return size;
	}
	
	public void setPos(float x, float y) {
		this.pos.set(x, y);
		if(this.gameObject.hasComponent(CCollision.class)) {
			CCollision collision = this.gameObject.getComponent(CCollision.class);
			CRender render = this.gameObject.getComponent(CRender.class);
			if(render.isAnchorCenter()) {
				collision.getBounds().setCenter(new Vector2f(x, y));
			}
			else {
				collision.getBounds().setCenter(new Vector2f(x + (size.x()/2), y + (size.y()/2)));
			}
		}
	}
	
	public void setSize(float width, float height) {
		this.size.set(width, height);
		if(this.gameObject.hasComponent(CCollision.class)) {
			CCollision collision = this.gameObject.getComponent(CCollision.class);
			collision.getBounds().setHalf_extent(new Vector2f(width, height));
		}
	}
	
	public void addPos(float x, float y) {
		this.pos.add(x, y);
		if(this.gameObject.hasComponent(CCollision.class)) {
			CCollision collision = this.gameObject.getComponent(CCollision.class);
			collision.getBounds().addToCenter(new Vector2f(x, y));
		}
	}
	
	public void addSize(float width, float height) {
		this.size.add(width, height);
		if(this.gameObject.hasComponent(CCollision.class)) {
			CCollision collision = this.gameObject.getComponent(CCollision.class);
			collision.getBounds().addToHalf_extent(new Vector2f(width, height));
		}
	}
}
