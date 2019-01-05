package Graphics.Models;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import Deprecated3D.Mesh;
import Deprecated3D.Model;
import Entities.Objects.Object2D;
import Graphics.Shaders;
import Graphics.Models.TextureSheet;
import Input.InputHandler;

public class Tiles extends Object2D {

	public Tiles(Texture texture, float x, float y, float width, float height, Vector4f color, boolean center_anchor, boolean destroyable) {
		super(width, height, color, center_anchor);
		// TODO Auto-generated constructor stub
		setTexture(texture);
		setPosition(x, y);
		setDestroyable(destroyable);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processInput(InputHandler handler) {
		// TODO Auto-generated method stub
		
	}
	
}