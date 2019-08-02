package DabEngine.Input;

import static org.lwjgl.glfw.GLFW.*;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import org.joml.Vector2d;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import DabEngine.Observer.ActionEventListener;
import DabEngine.Observer.ActionEventSender;

public class InputHandler extends ActionEventSender<ActionEventListener> {
	
	public static final InputHandler INSTANCE = new InputHandler();
	
	//private boolean[] keys = new boolean[65536];
	private int[] keys = new int[65536];
	private int[] buttons = new int[65536];

	private double xpos, ypos, dx, dy, lastx, lasty;
	
	public class Keyboard extends GLFWKeyCallback {

		@Override
		public void invoke(long arg0, int arg1, int arg2, int arg3, int arg4) {
			// TODO Auto-generated method stub
			if(arg1 == GLFW_KEY_UNKNOWN){
				return;
			}
			keys[arg1] = arg3;
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

			buttons[arg1] = arg3;
			
			dispatchMouseEvent(new MouseEvent(INSTANCE, arg1, arg2, arg3));
		}
		
	}
	
	public boolean isKeyPressed(int keycode) {
		return keys[keycode] != GLFW_RELEASE;
	}
	
	public boolean isKeyReleased(int keycode) {
		return keys[keycode] == GLFW_RELEASE;
	}
	
	public HashSet<Integer> lastPressedKeys() {
		HashSet<Integer> k = new HashSet<>();
		for(int i = 0; i < keys.length; i++) {
			if(keys[i] != GLFW_RELEASE) {
				k.add(i);
			}
		}
		return k;
	}

	public boolean isMousePressed(int buttoncode) {
		return buttons[buttoncode] != GLFW_RELEASE;
	}
	
	public boolean isMouseReleased(int buttoncode) {
		return buttons[buttoncode] == GLFW_RELEASE;
	}
	
	public Vector2d getMousePos() {
		return new Vector2d(xpos, ypos);
	}
	
	public Vector2d getMouseDelta() {
		Vector2d delta = new Vector2d(dx, dy);
		dx = 0;
		dy = 0;
		return delta;
	}
}
