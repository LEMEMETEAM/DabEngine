package DabEngine.Graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

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

	public static Vector4f worldToNDC(Vector3f pos){
		Vector4f p = new Vector4f(pos.x, pos.y,pos.z, 1.0f);
		p.mulProject(get());
		p.x /= p.w;
		p.y /= p.w;
		p.z /= p.w;

		p.z = p.z * 0.5f + 0.5f;
		System.out.println(p.toString());
		return p;
	}
	
	public static Matrix4f get() {
		return projectionmatrix.mul(viewmatrix, new Matrix4f()).mul(modelmatrix, new Matrix4f());
	}

}
