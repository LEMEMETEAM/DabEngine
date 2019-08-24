package DabEngine.Utils.Primitives;

import DabEngine.Graphics.Models.Mesh;

public interface Primitive<T> {
	
	T generate(float x, float y, float z);
	Mesh toMesh();
	
}
