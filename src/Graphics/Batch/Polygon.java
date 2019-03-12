package Graphics.Batch;

import org.joml.Vector2f;

public class Polygon {
	//Store points from -1 to 1 on x and y
	public Vector2f[] verts;
	public int[] inds;
	
	public Polygon(int[] indicies, Vector2f[] vertices) {
		verts = vertices;
		inds = indicies;
	}
}
