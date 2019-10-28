package DabEngine.GUI.Objects;

import java.util.ArrayList;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;

import DabEngine.GUI.GUIObject;
import DabEngine.Graphics.Graphics;
import DabEngine.Input.KeyEvent;
import DabEngine.Input.MouseEvent;
import DabEngine.Input.MouseMoveEvent;
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
	public void onKeyDown(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyUp(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseButtonDown(MouseEvent e) {
		// TODO Auto-generated method stub
		if (state.getState() == States.HOVER) {
			state.setState(States.PRESSED);
		}
	}

	@Override
	public void onMouseButtonUp(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Graphics g) {
		for (GUIObject obj : panel_objects) {
			obj.render(g);
		}
	}

	@Override
	public void update() {

	}

	@Override
	public void onMouseMove(MouseMoveEvent e) {
		// TODO Auto-generated method stub
		if(moveable && state.getState() == States.PRESSED)
		{
			Vector2d m = e.getMouseDelta();
			pos = new Vector3f().set((float)m.x, (float)m.y, pos.z);
		}
	}
	
}
