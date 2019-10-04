package DabEngine.Cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import DabEngine.Core.IDisposable;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Graphics.OpenGL.Textures.TextureLoader;
import DabEngine.Graphics.OpenGL.Textures.Texture.Parameters;
import DabEngine.Utils.Pair;

public class ResourceCache {

	private InMemoryCache cache = new InMemoryCache.Builder().initialSize(16).maxSize(64).build();

	public <T> T get(String res, Class<T> type, Object... extras) {
		T resource = null;
		if((resource = cache.get(res)) == null) {
			switch (type.getSimpleName()) {
				case "Texture":
					TextureLoader loader;
					try {
						loader = new TextureLoader(new File(res));
					} catch (Exception tex_ex1) {
						try {
							loader = new TextureLoader(
									Thread.currentThread().getContextClassLoader().getResourceAsStream(res));
						} catch (Exception tex_ex2) {
							try {
								loader = new TextureLoader(ResourceCache.class.getResourceAsStream(res));
							} catch (Exception tex_ex3) {
								try {
									loader = new TextureLoader(
											ResourceCache.class.getResourceAsStream("/Textures/unavailable.jpg"));
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					} finally {
						loader.close();
					}
					Texture tex = new Texture(loader.pixels, loader.width, loader.height, false, Arrays.copyOf(extras, extras.length, Parameters[].class));
					resource = type.cast(tex);
					break;

				case "Shaders":
					String[] res_split = res.split("\\|");
					final StringBuilder builder = new StringBuilder();
					Pair[] defs = Arrays.copyOf(extras, extras.length, Pair[].class);
					builder.append(res).append("\\|").append(definesToString(defs));
					Shaders s = null;
					try {
						s = new Shaders(new File(res_split[0]), new File(res_split[1]), defs);
					} catch (Exception s_ex) {
						try {
							s = new Shaders(new File("/Shaders/default.vs"), new File("/Shaders/default.fs"),
									defs);
						} catch (NullPointerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					resource = type.cast(s);
					break;
			}
			cache.add(res, resource);
		}

		return resource;
	}

	private static String definesToString(Pair<String, String>... defines){
        final StringBuilder builder = new StringBuilder();
        for(var d:defines){
            builder.append(d.left).append("_");
        }
        return builder.toString();
    }
}
