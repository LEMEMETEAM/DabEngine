package DabEngine.Cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Graphics.OpenGL.Textures.TextureLoader;


public enum ResourceManager {

	INSTANCE;
	
	public Texture getTexture(String filename){
		Texture resource;
		if((resource = InMemoryCache.INSTANCE.<Texture>get(filename)) == null) {
			try{
				TextureLoader loader = new TextureLoader(new File(filename));
				resource = new Texture(loader.pixels, loader.width, loader.height);
			}catch(Exception e) {
				try {
					TextureLoader loader = new TextureLoader(ResourceManager.class.getResourceAsStream("/Textures/unavailable.jpg"));
					resource = new Texture(loader.pixels, loader.width, loader.height);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			add(filename, resource);
		}
		return resource;
	}
	
	public Texture getTextureFromStream(String filename){
		Texture resource;
		if((resource = InMemoryCache.INSTANCE.<Texture>get(filename)) == null) {
			try{
				TextureLoader loader = new TextureLoader((InputStream)new FileInputStream(new File(filename)));
				resource = new Texture(loader.pixels, loader.width, loader.height);
			}catch(Exception e) {
				try {
					TextureLoader loader = new TextureLoader(ResourceManager.class.getResourceAsStream("/Textures/unavailable.jpg"));
					resource = new Texture(loader.pixels, loader.width, loader.height);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			add(filename, resource);
		}
		return resource;
	}
	
	public static <T> void add(String filename, T resource) {
		InMemoryCache.INSTANCE.add(filename, resource, 10000);
	}
}
