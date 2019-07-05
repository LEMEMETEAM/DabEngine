package DabEngine.GUI;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import DabEngine.GUI.Objects.Panel;
import DabEngine.Graphics.Graphics;
import DabEngine.Input.InputHandler;
import DabEngine.Observer.ActionEventListener;
import DabEngine.Utils.Color;

public abstract class GUIObject implements ActionEventListener {
	
	protected boolean hover = false;
	public Vector3f pos;
	public Vector2f size;
	public int z_index;
	
	public Color color;
	
	public abstract void onHover();
	
	public abstract void onExit();
	
	public void onAddedToPanel(Panel p) {
		pos.add(p.pos);
		//color.mul(p.color);
	}
	
	public GUIObject() {
		InputHandler.INSTANCE.addObserver(this);
	}

	public abstract void render(Graphics g);
}
