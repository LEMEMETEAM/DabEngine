package Audio;

import java.util.HashMap;

public class AudioManager {
	
	private static HashMap<String, Audio> samples = new HashMap<>();
	
	private AudioManager() {}
	
	public static void addSample(String name, Audio sample) {
		samples.put(name, sample);
	}
	
	public static Audio getSample(String name) {
		return samples.get(name);
	}
}
