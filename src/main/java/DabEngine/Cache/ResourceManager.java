package DabEngine.Cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Graphics.OpenGL.Textures.TextureLoader;
import DabEngine.Graphics.OpenGL.Textures.Texture.Parameters;

/**
 * Resource managerthat uses the {@link InMemoryCache} to cache textures.
 */
public enum ResourceManager {

	INSTANCE;
	
	public Texture getTexture(String filename, Parameters... params){
		Texture resource;
		if((resource = InMemoryCache.INSTANCE.<Texture>get(filename)) == null) {
			try(TextureLoader loader = new TextureLoader(new File(filename))){
				if(params.length > 1)
					resource = new Texture(loader.pixels, loader.width, loader.height, params);
				else
					resource = new Texture(loader.pixels, loader.width, loader.height, Parameters.NEAREST_LINEAR);
			}catch(Exception e) {
				try(TextureLoader loader = new TextureLoader(ResourceManager.class.getResourceAsStream("/Textures/unavailable.jpg"))) {
					if(params.length > 1)
						resource = new Texture(loader.pixels, loader.width, loader.height, params);
					else
						resource = new Texture(loader.pixels, loader.width, loader.height, Parameters.NEAREST_LINEAR);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			add(filename, resource);
		}
		return resource;
	}
	
	public Texture getTextureFromStream(String filename, Parameters... params){
		Texture resource;
		if((resource = InMemoryCache.INSTANCE.<Texture>get(filename)) == null) {
			try(TextureLoader loader = new TextureLoader((InputStream)new FileInputStream(new File(filename)))){
				if(params.length < 1)
					resource = new Texture(loader.pixels, loader.width, loader.height, params);
				else
					resource = new Texture(loader.pixels, loader.width, loader.height, Parameters.NEAREST_LINEAR);
			}catch(Exception e) {
				try(TextureLoader loader = new TextureLoader(ResourceManager.class.getResourceAsStream("/Textures/unavailable.jpg"))){
					if(params.length < 1)
						resource = new Texture(loader.pixels, loader.width, loader.height, params);
					else
						resource = new Texture(loader.pixels, loader.width, loader.height, Parameters.NEAREST_LINEAR);
				} catch (Exception e1) {
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
