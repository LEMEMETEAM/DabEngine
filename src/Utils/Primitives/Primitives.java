package Utils.Primitives;

import org.joml.Vector2f;

import Graphics.Batch.Polygon;

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
		}
	),
	TRI(
		new Vector2f[] {
			new Vector2f(-1, 1),
			new Vector2f(-1, -1),
			new Vector2f(1, -1)
		},
		new int[] {
			0,1,2
		}
	);
	
	private Vector2f[] verts;
	private int[] idx;
	
	Primitives(Vector2f[] verts, int[] idx){
		this.verts = verts;
		this.idx = idx;
	}
	
	public Polygon generate() {
		return new Polygon(idx, verts);
	}
}
