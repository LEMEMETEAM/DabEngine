package Graphics.Models;

public class VertexAttrib {
	
	public final int location;
	public final String name;
	public final int numComponents;
	
	public VertexAttrib(int location, String name, int numComponents) {
		this.location = location;
		this.name = name;
		this.numComponents = numComponents;
	}
}
