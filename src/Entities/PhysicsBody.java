package Entities;

import org.joml.Vector4f;

public abstract class PhysicsBody extends GameObject {
	
	public enum BodyType {
		DYNAMIC, STATIC
	}
	private BodyType bodytype;

	public PhysicsBody(float width, float height, Vector4f color, boolean center_anchor) {
		super(width, height, color, center_anchor);
		// TODO Auto-generated constructor stub
	}

	public BodyType getBodyType() {
		return bodytype;
	}

	public void setBodyType(BodyType bodytype) {
		this.bodytype = bodytype;
	}

}
