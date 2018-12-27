package Input;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2d;
import org.lwjgl.glfw.*;

public class InputHandler implements GLFWKeyCallbackI, GLFWCursorPosCallbackI, GLFWMouseButtonCallbackI{
	
	private boolean[] keys = new boolean[65536];
	private boolean[] buttons = new boolean[3];
	private int last_key;
	private double xpos, ypos, dx, dy, lastx, lasty;
	private int last_button;
	
	@Override
	public void invoke(long arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		keys[arg1] = arg3 != GLFW_RELEASE;
		last_key = arg1;
	}
	
	@Override
	public void invoke(long arg0, double arg1, double arg2) {
		// TODO Auto-generated method stub
		xpos = arg1;
		ypos = arg2;
		
		dx = xpos - lastx;
		dy = ypos - lasty;
		
		lastx = xpos;
		lasty = ypos;
	}
	
	@Override
	public void invoke(long arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		buttons[arg1] = arg3 != GLFW_RELEASE;
		last_button = arg1;
	}
	
	public boolean isKeyPressed(int keycode) {
		return keys[keycode];
	}
	
	public boolean isKeyReleased(int keycode) {
		return keycode == last_key && keys[keycode] == false;
	}
	
	public boolean isMousePressed(int buttoncode) {
		return buttons[buttoncode];
	}
	
	public boolean isMouseReleased(int buttoncode) {
		return buttoncode == last_button && buttons[buttoncode] == false;
	}
	
	public Vector2d getMousePos() {
		return new Vector2d(xpos, ypos);
	}
	
	public Vector2d getMouseDelta() {
		return new Vector2d(dx, dy);
	}

	@Override
	public String getSignature() {
		// TODO Auto-generated method stub
		return GLFWKeyCallbackI.super.getSignature();
	}

	@Override
	public void callback(long args) {
		// TODO Auto-generated method stub
		GLFWKeyCallbackI.super.callback(args);
	}
}
