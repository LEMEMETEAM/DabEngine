package Cache;

import java.io.IOException;

import DabEngineResources.DabEngineResources;
import Graphics.Models.Texture;

public class ResourceManager {
	
	public static Texture getTexture(String filename){
		Texture resource;
		if((resource = InMemoryCache.getInstance().<Texture>get(filename)) == null) {
			try{
				resource = new Texture(filename);
			}catch(Exception e) {
				try {
					resource = new Texture(DabEngineResources.class, "Textures/unavailable.jpg");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			add(filename, resource);
		}
		return resource;
	}
	
	public static Texture getTextureFromResource(Class<DabEngineResources> resources, String filename){
		Texture resource;
		if((resource = InMemoryCache.getInstance().<Texture>get(filename)) == null) {
			try{
				resource = new Texture(resources, filename);
			}catch(Exception e) {
				try {
					resource = new Texture(DabEngineResources.class, "Textures/unavailable.jpg");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			add(filename, resource);
		}
		return resource;
	}
	
	public static <T> void add(String filename, T resource) {
		InMemoryCache.getInstance().add(filename, resource, 10000);
	}
}
