package DabEngine.GUI.Objects;

import org.joml.Vector3f;
import org.joml.Vector4f;

import DabEngine.GUI.GUIObject;
import DabEngine.Graphics.Graphics;
import DabEngine.Input.KeyEvent;
import DabEngine.Input.MouseEvent;
import DabEngine.Input.MouseMoveEvent;
import DabEngine.Observer.Event;
import DabEngine.Resources.Textures.Texture;

public class Image extends GUIObject {

	public Texture image;

	@Override
	public void render(Graphics g) {
		g.setTexture(image);
		g.drawQuad(pos, new Vector3f(size.x, size.y, 0), new Vector3f(0), new Vector4f(0), color);
	}

	@Override
	public void update() {

	}

	@Override
	public void onKeyDown(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyUp(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNotify(Event e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseButtonDown(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseButtonUp(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseMove(MouseMoveEvent e) {
		// TODO Auto-generated method stub

	}

}
