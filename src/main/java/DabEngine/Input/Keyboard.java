package DabEngine.Input;

import static org.lwjgl.glfw.GLFW.*;

import DabEngine.Observer.IEventSender;

public class Keyboard extends IEventSender<KeyboardEventListener>
{

	public void onKeyDown(int keycode, int scancode, int mods)
	{
		KeyEvent e = new KeyEvent(keycode, scancode, GLFW_PRESS, mods);

		for(KeyboardEventListener l : observers)
		{
			l.onNotify(e);
		}
	}

	public void onKeyUp(int keycode, int scancode, int mods)
	{
		KeyEvent e = new KeyEvent(keycode, scancode, GLFW_RELEASE, mods);

		for(KeyboardEventListener l : observers)
		{
			l.onNotify(e);
		}
	}
}