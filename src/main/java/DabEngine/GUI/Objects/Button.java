package DabEngine.GUI.Objects;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import DabEngine.GUI.GUIObject;
import DabEngine.Graphics.Graphics;
import DabEngine.Input.KeyEvent;
import DabEngine.Input.MouseEvent;
import DabEngine.Input.MouseMoveEvent;
import DabEngine.Observer.Event;
import DabEngine.Resources.Font.Font;
import DabEngine.Resources.Textures.Texture;
import DabEngine.Utils.Color;

public class Button extends GUIObject {

	public String label;
	public Color label_color;
	public boolean show_button = true, show_label = true;
	public Vector2f label_pos;
	public Font font;
	// optional
	public Texture buttonTexture;

	@Override
	public void onNotify(Event e) {
		// TODO Auto-generated method stub

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
	public void onMouseButtonDown(MouseEvent e) {
		// TODO Auto-generated method stub
		if (state.getState() == States.HOVER && e.getButton() == GLFW_MOUSE_BUTTON_LEFT) {
			state.setState(States.PRESSED);
		}
	}

	@Override
	public void onMouseButtonUp(MouseEvent e) {
		// TODO Auto-generated method stub
		if (state.getState() == States.PRESSED)
			state.setState(States.RELEASED);
	}

	@Override
	public void render(Graphics g) {
		if (buttonTexture != null) {
			g.setTexture(buttonTexture);
			g.drawQuad(null, new Vector3f(pos.x - size.x / 2, pos.y - size.y / 2, pos.z),
					new Vector3f(size.x, size.y, 0), new Vector3f(0), new Vector4f(0), Color.WHITE);
		} else {
			g.setTexture(null);
			g.drawQuad(null, new Vector3f(pos.x - size.x / 2, pos.y - size.y / 2, pos.z),
					new Vector3f(size.x, size.y, 0), new Vector3f(0), new Vector4f(0), color);
			// g.pushShader(font.shader);
			// g.drawText(font, label, (size.x * label_pos.x) + pos.x, (size.y *
			// label_pos.y) + pos.y, pos.z+0.1f, label_color);
			// g.popShader();
		}
	}

	@Override
	public void update() {

	}

	@Override
	public void onMouseMove(MouseMoveEvent e) {
		// TODO Auto-generated method stub

	}
	
	
}
