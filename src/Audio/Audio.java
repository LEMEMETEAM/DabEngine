package Audio;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio {
	
	private Clip clip;
	private int loop;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public Audio(String filename) {
		try {
			File file = new File(filename);
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);
			
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (UnsupportedAudioFileException | IOException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
		} catch (LineUnavailableException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
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
	
	public long getSamplePos() {
		if(clip.isOpen()) {
			return clip.getMicrosecondPosition();
		}
		return 0;
	}
	
	public void setLoop(int loop) {
		this.loop = loop;
	}
}
