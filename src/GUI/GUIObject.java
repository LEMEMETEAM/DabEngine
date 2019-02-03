package GUI;

import Entities.GameObject;
import Observer.Observer;

public abstract class GUIObject extends GameObject implements Observer {
	
	protected boolean hover = false;
	
	public abstract void onHover();
	
	public abstract void onExit();
}
