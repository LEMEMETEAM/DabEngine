package Scenes;

public abstract class Transition {

	public float in_transition_time, out_transition_time;
	public boolean in, out;
	
	public abstract void in();
	public abstract void out();
}
