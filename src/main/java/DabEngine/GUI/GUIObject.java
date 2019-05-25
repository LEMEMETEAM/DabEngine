package DabEngine.GUI;

import org.joml.Vector2f;
import org.joml.Vector4f;

import DabEngine.Input.InputHandler;
import DabEngine.Observer.ActionEventListener;

public abstract class GUIObject implements ActionEventListener {
	
	protected boolean hover = false;
	public Vector2f pos;
	public Vector2f size;
	
	public Vector4f color;
	
	public abstract void onHover();
	
	public abstract void onExit();
	
	public void onAddedToPanel(Panel p) {
		pos.add(p.pos);
		//color.mul(p.color);
	}
	
	public GUIObject() {
		InputHandler.INSTANCE.addObserver(this);
	}
}
