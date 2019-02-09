package GUI.Objects;

import org.newdawn.slick.TrueTypeFont;

import Entities.Components.CTransform;
import GUI.GUIObject;
import GUI.Components.CText;
import Input.KeyEvent;
import Input.MouseEvent;
import Observer.Event;

public class Text extends GUIObject {
	
	public Text() {
		addComponent(new CText());
		addComponent(new CTransform());
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

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
	}
	
}
