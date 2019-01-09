package States;

public interface Transition {
	boolean playing = false;
	public void in();
	public void out();
	public default boolean isPlaying() {
		return playing;
	}
}
