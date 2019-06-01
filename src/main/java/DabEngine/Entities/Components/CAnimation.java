package DabEngine.Entities.Components;

import java.util.ArrayList;

public class CAnimation extends Component {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3196656268764668648L;
	public int currentFrame;
	public int texSheetFrame;
	public boolean looped;
	public ArrayList<Integer> frames = new ArrayList<>();
	public ArrayList<Integer> delays = new ArrayList<>();
	
	public CAnimation() {
		currentFrame = 0;
		texSheetFrame = 0;
		looped = false;
	}
	
	public int getCurrentIndex() {
		return frames.get(texSheetFrame);
	}
	
	public void add(int frame, int delay) {
		frames.add(frame);
		delays.add(delay);
	}
	
	public void setLooping(boolean looping) {
		this.looped = looping;
	}
	
	public boolean getLooping() {
		return looped;
	}
}
