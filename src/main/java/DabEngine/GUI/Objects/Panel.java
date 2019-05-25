package DabEngine.GUI.Objects;

import java.util.ArrayList;

import org.joml.Vector2f;

import DabEngine.GUI.GUIObject;
import DabEngine.Input.InputHandler;
import DabEngine.Input.KeyEvent;
import DabEngine.Input.MouseEvent;
import DabEngine.Observer.Event;

public class Panel extends GUIObject {
	
	protected boolean moveable, scaleable;
	public final ArrayList<GUIObject> panel_objects = new ArrayList<>();
	
	public Panel(boolean moveable, boolean scaleable) {
		this.moveable = moveable;
		this.scaleable = scaleable;
	}
	
	public Panel(boolean moveable) {
		this(moveable, false);
	}
	
	public Panel() {
		this(false, false);
	}
	
	public void addToPanel(GUIObject g) {
		panel_objects.add(g);
		g.onAddedToPanel(this);
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
		if(hover && moveable) {
			pos = new Vector2f().set(InputHandler.INSTANCE.getMouseDelta());
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
	
}
