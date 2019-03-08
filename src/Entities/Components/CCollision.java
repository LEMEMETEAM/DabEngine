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
    	CRender render = this.entity.getComponent(CRender.class);
		CTransform transform = this.entity.getComponent(CTransform.class);
		if(render.center_anchor) {
			this.entity.getComponent(CCollision.class).getBounds().setCenter(transform.pos);
		} else {
			this.entity.getComponent(CCollision.class).getBounds().setCenter(new Vector2f(transform.pos.x() + (transform.size.x()/2),
					transform.pos.y() + (transform.size.y()/2)));
		}
		this.entity.getComponent(CCollision.class).getBounds().setHalf_extent(new Vector2f(transform.size.x()/2, transform.size.y()/2));
    }
}
