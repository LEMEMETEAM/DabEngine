package DabEngine.GUI;

import org.joml.Vector2f;
import org.joml.Vector3f;

import DabEngine.GUI.Objects.Panel;
import DabEngine.Graphics.Graphics;
import DabEngine.Input.KeyboardEventListener;
import DabEngine.Input.MouseEventListener;
import DabEngine.States.State;
import DabEngine.States.StateManager;
import DabEngine.Utils.Color;

public abstract class GUIObject implements KeyboardEventListener, MouseEventListener {
	
	public Vector3f pos;
	public Vector2f size;
	
	public Color color;

	public StateManager state = new StateManager();
	public enum States implements State{
		PRESSED(false),
		RELEASED(false),
		HOVER(true),
		EXIT(true),
		ACTION(false);

		private boolean interruptable;
		public boolean finished;

		States(boolean i){
			interruptable = i;
		}

		@Override
		public boolean isInterruptable() {
			return interruptable;
		}

		@Override
		public boolean isFinished() {
			return finished;
		}

		@Override
		public void setFinished(boolean finished){
			this.finished = finished;
		}
	}

	public enum AnchorPoint {
		TOP, RIGHT, BOTTOM, LEFT,
		TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT;
	}

	public AnchorPoint anchor;
	
	public void onAddedToPanel(Panel p) {
		pos.add(p.pos);
		//color.mul(p.color);
	}

	public abstract void render(Graphics g);
	public abstract void update();
}
