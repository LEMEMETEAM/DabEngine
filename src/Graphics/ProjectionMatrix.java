package Graphics;

import org.joml.Matrix4f;

public class ProjectionMatrix {
	
	private static Matrix4f projmatrix;
	
	private ProjectionMatrix() {}
	
	public static ProjectionMatrix createProjectionMatrix2D(float left, float right, float bottom, float top) {
		projmatrix = new Matrix4f().ortho(left, right, bottom, top, -1, 1);
		return null;
	}
	
	public static Matrix4f get() {
		return projmatrix;
	}

}
