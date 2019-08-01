package DabEngine.Audio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Music {
	
	private SourceDataLine soundLine;
	private final int BUFFER_SIZE = 64*1024;
	private AudioInputStream ais;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private Thread musicThread;
	/**
	 * A Buffered Audio stream  that reads from a file and can be played, stopped and looped.
	 * Used mostly for large audio files like music that cannot be saved in memory.
	 * @param file the file to read from.
	 */
	public Music(File file) {
		try {
			ais = AudioSystem.getAudioInputStream(file);
			AudioFormat format = ais.getFormat();
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			
			soundLine = (SourceDataLine) AudioSystem.getLine(info);
			soundLine.open(format);
			
		} catch (UnsupportedAudioFileException | IOException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
		} catch (LineUnavailableException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
	}

	/**
	 * A Buffered Audio stream  that reads from a file and can be played, stopped and looped.
	 * Used mostly for large audio files like music that cannot be saved in memory.
	 * @param stream the inputstream to read from.
	 */
	public Music(InputStream stream) {
		try {
			ais = AudioSystem.getAudioInputStream(stream);
			AudioFormat format = ais.getFormat();
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			
			soundLine = (SourceDataLine) AudioSystem.getLine(info);
			soundLine.open(format);
			
		} catch (UnsupportedAudioFileException | IOException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
		} catch (LineUnavailableException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	/**
	 * Plays the music in a new thread so it doesn't block other parts of code.
	 */
	public void play() {
		musicThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					soundLine.start();
					byte[] sampledData = new byte[BUFFER_SIZE];
					int bytesRead = 0;
					while(bytesRead != -1) {
						bytesRead = ais.read(sampledData, 0, sampledData.length);
						if(bytesRead >= 0) {
							soundLine.write(sampledData, 0, bytesRead);
						}
					}
				} catch(IOException e) {
					LOGGER.log(Level.WARNING, "IO Error", e);
				}
				finally {
					soundLine.drain();
					soundLine.close();
				}
			}
		});
		musicThread.start();
	}
	
	/**
	 * Stops the audio, flushes the data from the line and closes the thread
	 */
	public void stop() {
		soundLine.stop();
		soundLine.flush();
		try {
			musicThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Just stops the audio but doesn't flush the audio line so it can be started again
	 */
	public void pauseSample() {
		soundLine.stop();
	}
	
	/**
	 * Get current position, in milliseconds, of audio.
	 * @return position
	 */
	public long getSamplePos() {
		if(soundLine.isOpen()) {
			return soundLine.getMicrosecondPosition();
		}
		return 0;
	}
}
