package Graphics;

import java.util.ArrayList;

public class Animation {
	
	private int currentFrame;
	private int texSheetFrame;
	private boolean looped;
	private ArrayList<Integer> frames = new ArrayList<>();
	private ArrayList<Integer> delays = new ArrayList<>();
	
	public Animation() {
		currentFrame = 0;
		texSheetFrame = 0;
		looped = false;
	}
	
	public void update() {
		currentFrame++;
		if(currentFrame >= delays.get(texSheetFrame)) {
			texSheetFrame++;
			if(texSheetFrame > frames.size() - 1) {
				texSheetFrame = looped ? 0 : frames.size();
			}
			currentFrame = 0;
		}
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
