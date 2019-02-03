package GUI.Objects;

import GUI.GUIObject;
import GUI.Components.CContainer;
import Input.KeyEvent;
import Input.MouseEvent;
import Observer.Event;

public class Panel extends GUIObject {
	
	public Panel() {
		addComponent(new CContainer());
	}

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
		
	}

	@Override
	public void onMouseRelease(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHover() {
		// TODO Auto-generated method stub
		
	}
	
}
