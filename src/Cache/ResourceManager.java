package Cache;

import Graphics.Models.Texture;

public class ResourceManager {
	
	public static Texture getTexture(String filename){
		Texture resource;
		if((resource = InMemoryCache.getInstance().<Texture>get(filename)) == null) {
			resource = new Texture(filename);
			add(filename, resource);
		}
		return resource;
	}
	
	public static <T> void add(String filename, T resource) {
		InMemoryCache.getInstance().add(filename, resource, 10000);
	}
}
