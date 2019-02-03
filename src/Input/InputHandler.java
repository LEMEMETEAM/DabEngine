package Input;

import static org.lwjgl.glfw.GLFW.*;

import java.util.HashMap;

import org.joml.Vector2d;
import org.lwjgl.glfw.*;

import Observer.Subject;

public class InputHandler extends Subject {
	
	public static final InputHandler INSTANCE = new InputHandler();
	
	//private boolean[] keys = new boolean[65536];
	private HashMap<Integer, Integer> keys = new HashMap<>() {{
		for(int i = 0; i < 65536; i++) {
			put(i, GLFW_RELEASE);
		}
	}};
	private boolean[] buttons = new boolean[3];
	private int last_key;
	private double xpos, ypos, dx, dy, lastx, lasty;
	private int last_button;
	
	public class Keyboard extends GLFWKeyCallback {

		@Override
		public void invoke(long arg0, int arg1, int arg2, int arg3, int arg4) {
			// TODO Auto-generated method stub
			keys.put(arg1, arg3);
			/*keys[arg1] = arg3 != GLFW_RELEASE;
			last_key = arg1;*/
			dispatchKeyEvent(new KeyEvent(INSTANCE, arg1, arg2, arg3, arg4));
		}
		
	}
	
	public class MousePos extends GLFWCursorPosCallback {

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
		
	}
	
	public class MouseButton extends GLFWMouseButtonCallback {

		@Override
		public void invoke(long arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			buttons[arg1] = arg3 != GLFW_RELEASE;
			last_button = arg1;
			
			dispatchMouseEvent(new MouseEvent(INSTANCE, arg1, arg2, arg3));
		}
		
	}
	
	public boolean isKeyPressed(int keycode) {
		return keys.get(keycode) != GLFW_RELEASE;
	}
	
	public boolean isKeyReleased(int keycode) {
		return keys.get(keycode) == GLFW_RELEASE;
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
}
