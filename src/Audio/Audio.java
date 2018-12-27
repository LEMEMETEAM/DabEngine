package Audio;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import Graphics.Models.Renderable2D;
import Observer.Observer;

public class Audio implements Observer {
	
	private Clip clip;
	private int loop;
	
	public Audio(String filename) {
		try {
			File file = new File(filename);
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);
			
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void playSample() {
		if(clip.isRunning()) {
			clip.stop();
		}
		clip.setFramePosition(0);
		clip.loop(loop);
	}
	
	public void stopSample() {
		if(clip.isRunning()) {
			clip.stop();
		}
		clip.setFramePosition(0);
	}
	
	public void pauseSample() {
		if(clip.isRunning()) {
			clip.stop();
		}
	}
	
	public void setLoop(int loop) {
		this.loop = loop;
	}

	@Override
	public void onNotify(String s) {
		// TODO Auto-generated method stub
	}
}
