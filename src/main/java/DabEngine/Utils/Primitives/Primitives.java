package DabEngine.Utils.Primitives;

import org.joml.Vector2f;

import Graphics.Batch.Polygon;

import static org.lwjgl.opengl.GL11.*;

public enum Primitives {
	QUAD(
		new Vector2f[] {
			new Vector2f(-1, 1),
			new Vector2f(-1, -1),
			new Vector2f(1, -1),
			new Vector2f(1, 1)
		},
		new int[] {
			0,1,2,
			0,3,2
		},
		GL_TRIANGLES
	),
	TRI(
		new Vector2f[] {
			new Vector2f(-1, 1),
			new Vector2f(-1, -1),
			new Vector2f(1, -1)
		},
		new int[] {
			0,1,2
		},
		GL_TRIANGLES
	);
	
	private Vector2f[] verts;
	private int[] idx;
	private int type;
	
	Primitives(Vector2f[] verts, int[] idx, int type){
		this.verts = verts;
		this.idx = idx;
		this.type = type;
	}
	
	public Polygon generate() {
		return new Polygon(idx, verts);
	}
}
