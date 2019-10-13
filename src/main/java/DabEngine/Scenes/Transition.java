package DabEngine.Scenes;

import DabEngine.Graphics.Graphics;

public abstract class Transition {

	private float duration;
	private float time;

	public Transition(float duration){
		this.duration = duration;
	}

	public abstract void render(Graphics g);

	public boolean isFinished(){
		return this.time > this.duration;
	}
}
