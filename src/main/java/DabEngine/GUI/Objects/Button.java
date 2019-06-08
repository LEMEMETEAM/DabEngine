package DabEngine.GUI.Objects;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import org.joml.Vector2f;
import org.joml.Vector4f;

import DabEngine.GUI.GUIObject;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.Batch.Font;
import DabEngine.Graphics.Batch.Polygon;
import DabEngine.Input.KeyEvent;
import DabEngine.Input.MouseEvent;
import DabEngine.Observer.Event;
import DabEngine.Utils.Color;

public class Button extends GUIObject {

	public String label;
	public Color label_color;
	public boolean show_button = true, show_label = true;
	public Vector2f label_pos;
	public Font font;

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
		if (hover && e.getButton() == GLFW_MOUSE_BUTTON_LEFT) {
			action();
		}
	}

	@Override
	public void onMouseRelease(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onHover() {
		// TODO Auto-generated method stub
		hover = true;
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		hover = false;
	}

	public void action() {

	}

	@Override
	public void render(Graphics g) {
		g.fillRect(pos.x, pos.y, size.x, size.y, 0, 0, 0, color);
		g.pushShader(Font.TEXT_DEFAULT_SHADER);
		g.drawText(font, label, ((size.x - pos.x) * label_pos.x) + pos.x, ((size.y - pos.y) * label_pos.y) + pos.y, label_color);
		g.popShader();
	}
	
	
}
