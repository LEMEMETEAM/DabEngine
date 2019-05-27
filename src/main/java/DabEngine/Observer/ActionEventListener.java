package DabEngine.Observer;

import DabEngine.Input.KeyEvent;
import DabEngine.Input.MouseEvent;

public interface ActionEventListener extends IEventListener {
	public void onKeyPress(KeyEvent e);
	public void onKeyRelease(KeyEvent e);
	public void onMousePress(MouseEvent e);
	public void onMouseRelease(MouseEvent e);
}
