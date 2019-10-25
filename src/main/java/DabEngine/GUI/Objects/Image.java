package DabEngine.GUI.Objects;

import org.joml.Vector3f;
import org.joml.Vector4f;

import DabEngine.GUI.GUIObject;
import DabEngine.Graphics.Graphics;
import DabEngine.Input.KeyEvent;
import DabEngine.Input.MouseEvent;
import DabEngine.Observer.Event;
import DabEngine.Resources.Textures.Texture;

public class Image extends GUIObject {

	public Texture image;

	@Override
	public void onNotify(Event e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyPress(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyRelease(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMousePress(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseRelease(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Graphics g) {
		g.setTexture(image);
		g.drawQuad(pos, new Vector3f(size.x, size.y, 0), new Vector3f(0), new Vector4f(0), color);
	}

	@Override
	public void update() {

	}

}
