package DabEngine.Scenes;

public abstract class Transition {

	public float in_transition_time, out_transition_time;
	
	public abstract void initIn();
	public abstract void in(int counter);
	public abstract void initOut();
	public abstract void out(int counter);
	public abstract void cleanUp();
}
