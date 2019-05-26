package DabEngine.Cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import DabEngine.Graphics.Models.Texture;

public class ResourceManager {
	
	public static Texture getTexture(String filename){
		Texture resource;
		if((resource = InMemoryCache.getInstance().<Texture>get(filename)) == null) {
			try{
				resource = new Texture(new File(filename));
			}catch(Exception e) {
				try {
					resource = new Texture(ResourceManager.class.getResourceAsStream("/Textures/unavailable.jpg"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			add(filename, resource);
		}
		return resource;
	}
	
	public static Texture getTextureFromStream(String filename){
		Texture resource;
		if((resource = InMemoryCache.getInstance().<Texture>get(filename)) == null) {
			try{
				resource = new Texture((InputStream)new FileInputStream(new File(filename)));
			}catch(Exception e) {
				try {
					resource = new Texture(ResourceManager.class.getResourceAsStream("/Textures/unavailable.jpg"));
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
