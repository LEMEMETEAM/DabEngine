package DabEngine.Scenes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Stack;

import org.joml.Vector4f;

import DabEngine.Core.App;
import DabEngine.Graphics.Camera;
import DabEngine.Graphics.Camera2D;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.Batch.SortType;
import DabEngine.Graphics.OpenGL.RenderTarget;
import DabEngine.Graphics.OpenGL.Light.Light;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Graphics.OpenGL.Textures.TextureRegion;
import DabEngine.Graphics.OpenGL.Textures.Texture.Parameters;
import DabEngine.Graphics.OpenGL.Viewport.Viewport;
import DabEngine.System.ComponentSystem;
import DabEngine.Utils.Color;
import DabEngine.Utils.FixedArrayList;
import DabEngine.Utils.Pair;

public abstract class Scene {
	private LinkedHashSet<ComponentSystem> sys = new LinkedHashSet<>();
	public ArrayDeque<Overlay> overlays = new ArrayDeque<>();
	public App app;
	public final int MAX_LIGHTS = 32;
	public float ambientStrength;
	public boolean paused;
	public SceneConfig config;
	public RenderTarget rt;
	public TextureRegion inv_uv = new TextureRegion();
	
	protected Scene(App app, SceneConfig config){
		this.app = app;
		this.config = config;
		if(config.renderToTexture){
			ArrayList<Texture> textures = new ArrayList<>();
			for(int i = 0; i < config.renderTextureAmmount; i++){
				textures.add(new Texture(null, app.WIDTH, app.HEIGHT, config.srgb, Parameters.LINEAR));
			}
			rt = new RenderTarget(app.WIDTH, app.HEIGHT, app.vp, textures.toArray(new Texture[textures.size()]));
			inv_uv.uv = new Vector4f(0,1,1,0);
		}
	}
	
	public Scene(App app){
		
	}

	public void render(Graphics g) {
		for(ComponentSystem system : sys) {
			system.render(g);
		}
		for(Overlay s : overlays){
			s.render(g);
		}
	}

	public void update() {
		if(!paused){
			for(ComponentSystem system : sys) {
				system.update();
			}
		}
		for(Overlay s : overlays){
			s.update();
		}
	}

	public void addSystem(ComponentSystem system) {
		sys.add(system);
		system.addedToScene(this);
	}
	
	public <T> T getSystem(Class<T> clazz) {
		for(ComponentSystem system : sys) {
			if(clazz.isAssignableFrom(system.getClass())) {
				return clazz.cast(system);
			}
		}
		return null;
	}

	public void pushOverlay(Overlay overlay){
		overlays.push(overlay.addedToScene(this));
	}

	public void popOverlay(){
		overlays.pop();
	}

	public void removeOverlay(Overlay overlay){
		overlays.remove(overlay);
	}
	
	public abstract void init();
}
