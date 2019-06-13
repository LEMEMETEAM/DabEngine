package DabEngine.Scenes;

import DabEngine.Graphics.Graphics;

public abstract class Transition {

	public float in_transition_time, out_transition_time;
	
	public abstract void in(Graphics g, double time);
	public abstract void out(Graphics g, double time);
	public abstract void cleanUp();
}
