package Observer;

import Input.KeyEvent;
import Input.MouseEvent;

public interface Observer {
	public void onNotify(Event e);
	public void onKeyPress(KeyEvent e);
	public void onKeyRelease(KeyEvent e);
	public void onMousePress(MouseEvent e);
	public void onMouseRelease(MouseEvent e);
}
