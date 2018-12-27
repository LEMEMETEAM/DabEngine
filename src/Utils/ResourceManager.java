package Utils;

import java.util.HashMap;

import Graphics.Shaders;
import Graphics.Models.Texture;

public class ResourceManager {
	
	private static HashMap<String, Shaders> shaders;
	private static HashMap<String, Texture> textures;
	
	private ResourceManager() {}
	
	static {
		shaders = new HashMap<>();
		textures = new HashMap<>();
	}
	
	public static ResourceManager addShader(String name, Shaders shader) {
		shaders.put(name, shader);
		return null;
	}
	
	public static ResourceManager addTexture(String name, Texture texture) {
		textures.put(name, texture);
		return null;
	}
	
	//does the exact same as add, only used for readability
	public static ResourceManager replaceShader(String nametoreplace, Shaders shader) {
		shaders.put(nametoreplace, shader);
		return null;
	}
	
	public static ResourceManager replaceTexture(String nametoreplace, Texture texture) {
		textures.put(nametoreplace, texture);
		return null;
	}
	
	public static Shaders getShader(String name) {
		return shaders.get(name);
	}
	
	public static Texture getTexture(String name) {
		return textures.get(name);
	}
	
	public static HashMap getShaders() {
		return shaders;
	}
	
	public static HashMap getTextures() {
		return textures;
	}
}
