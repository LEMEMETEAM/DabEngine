package DabEngine.Graphics;

import org.joml.Matrix4f;

public class ProjectionMatrix {
	
	private static Matrix4f projectionmatrix = new Matrix4f();
	private static Matrix4f viewmatrix = new Matrix4f();
	private static Matrix4f modelmatrix = new Matrix4f();
	
	private ProjectionMatrix() {}
	
	public static ProjectionMatrix createProjectionMatrix2D(float left, float right, float bottom, float top) {
		projectionmatrix = new Matrix4f().ortho(left, right, bottom, top, -1, 1);
		return null;
	}
	
	public static ProjectionMatrix createProjectionMatrix3D(float FOV, float aspectRatio, float near, float far) {
		projectionmatrix = new Matrix4f().perspective(FOV, aspectRatio, near, far);
		return null;
	}

	public static ProjectionMatrix addViewMatrix(Matrix4f view){
		viewmatrix = view;
		return null;
	}

	public static ProjectionMatrix addModelMatrix(Matrix4f model){
		modelmatrix = model;
		return null;
	}
	
	public static Matrix4f get() {
		return projectionmatrix.mul(viewmatrix).mul(modelmatrix);
	}

}
