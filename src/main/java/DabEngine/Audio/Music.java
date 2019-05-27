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
	
	
	public void play() {
		new Thread(new Runnable() {
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
					e.printStackTrace();
				}
				finally {
					soundLine.drain();
					soundLine.close();
				}
			}
		}).start();
	}
	
	public void stop() {
		soundLine.stop();
		soundLine.flush();
	}
	
	public void pauseSample() {
		soundLine.stop();
	}
	
	public long getSamplePos() {
		if(soundLine.isOpen()) {
			return soundLine.getMicrosecondPosition();
		}
		return 0;
	}
}
