package GUI.Objects;

import GUI.GUIObject;
import Graphics.Batch.Polygon;
import Input.KeyEvent;
import Input.MouseEvent;
import Observer.Event;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Button extends GUIObject {
	
	public Polygon poly;
	public String label;
	public Vector4f label_color;
	public boolean show_button = true, show_label = true;
	public Vector2f label_pos;
	public float label_size;

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
		if(hover && e.getButton() == GLFW_MOUSE_BUTTON_LEFT) {
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
	
	
}
