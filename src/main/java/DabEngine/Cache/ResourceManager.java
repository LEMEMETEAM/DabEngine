package DabEngine.Cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Graphics.OpenGL.Textures.TextureLoader;
import DabEngine.Graphics.OpenGL.Textures.Texture.Parameters;
import DabEngine.Utils.Pair;

/**
 * Resource managerthat uses the {@link InMemoryCache} to cache textures and shaders.
 */
public enum ResourceManager {

	INSTANCE;
	
	public Texture getTexture(String filename, Parameters... params){
		Texture resource;
		if((resource = InMemoryCache.INSTANCE.<Texture>get(filename)) == null) {
			try(TextureLoader loader = new TextureLoader(new File(filename))){
				if(params.length > 1)
					resource = new Texture(loader.pixels, loader.width, loader.height, false, params);
				else
					resource = new Texture(loader.pixels, loader.width, loader.height, false, Parameters.NEAREST_LINEAR);
			}catch(Exception e) {
				try(TextureLoader loader = new TextureLoader(Thread.currentThread().getContextClassLoader().getResourceAsStream(filename))){
					if(params.length >1)
						resource = new Texture(loader.pixels, loader.width, loader.height, false, params);
					else
						resource = new Texture(loader.pixels, loader.width, loader.height, false, params);
				}
				catch(Exception e2){
					try(TextureLoader loader = new TextureLoader(ResourceManager.class.getResourceAsStream("/Textures/unavailable.jpg"))) {
						if(params.length > 1)
							resource = new Texture(loader.pixels, loader.width, loader.height, false, params);
						else
							resource = new Texture(loader.pixels, loader.width, loader.height, false, Parameters.NEAREST_LINEAR);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			add(filename, resource);
		}
		return resource;
	}
	
	public Shaders getShader(String shader_vs_dir, String shader_fs_dir, Pair<String, String>... defines){
        String name_vs = shader_vs_dir.substring(shader_vs_dir.lastIndexOf("/")+1);
        String name_fs = shader_fs_dir.substring(shader_fs_dir.lastIndexOf("/")+1);
        Shaders s;
        final StringBuilder builder = new StringBuilder();
        builder.append(name_vs).append(name_fs).append(definesToString(defines));
        if((s = InMemoryCache.INSTANCE.get(builder.toString())) == null){
			try{
				s = new Shaders(new File(shader_vs_dir), new File(shader_fs_dir), defines);
			} catch (Exception e) {
				try {
					s = new Shaders(new File("/Shaders/default.vs"), new File("/Shaders/default.fs"),
							new Pair<>("UNSHADED", "0"));
				} catch (NullPointerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
            InMemoryCache.INSTANCE.add(builder.toString(), s, 1000000);
        }
        return s;
    }

    private static String definesToString(Pair<String, String>... defines){
        final StringBuilder builder = new StringBuilder();
        for(var d:defines){
            builder.append(d.left).append("_");
        }
        return builder.toString();
    }
	
	private static <T> void add(String filename, T resource) {
		InMemoryCache.INSTANCE.add(filename, resource, 10000);
	}
}
