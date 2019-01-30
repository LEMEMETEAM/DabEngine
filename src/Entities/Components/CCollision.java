package Entities.Components;

import org.joml.Vector2f;

import Graphics.Models.AABB;

public class CCollision extends Component {
	
	private AABB bounds;
	
	public CCollision() {
		bounds = new AABB();
	}
	
	public AABB getBounds() {
		return bounds;
	}
	
	public void correctBounds() {
    	CRender render = this.gameObject.get().getComponent(CRender.class);
		CTransform transform = this.gameObject.get().getComponent(CTransform.class);
		if(render.isAnchorCenter()) {
			this.gameObject.get().getComponent(CCollision.class).getBounds().setCenter(transform.getPos());
		} else {
			this.gameObject.get().getComponent(CCollision.class).getBounds().setCenter(new Vector2f(transform.getPos().x() + (transform.getSize().x()/2),
					transform.getPos().y() + (transform.getSize().y()/2)));
		}
		this.gameObject.get().getComponent(CCollision.class).getBounds().setHalf_extent(new Vector2f(transform.getSize().x()/2, transform.getSize().y()/2));
    }
}
