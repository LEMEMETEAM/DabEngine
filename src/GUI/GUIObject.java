package GUI;

import org.joml.Vector2f;
import org.joml.Vector4f;

import Graphics.Models.Texture;
import Input.InputHandler;
import Observer.Observer;

public abstract class GUIObject implements Observer {
	
	protected boolean hover = false;
	public Vector2f pos;
	public Vector2f size;
	
	public Texture tex;
	public Vector4f color;
	
	public abstract void onHover();
	
	public abstract void onExit();
	
	public GUIObject() {
		InputHandler.INSTANCE.addObserver(this);
	}
}
