package DabEngine.Scenes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Stack;

import DabEngine.Core.App;
import DabEngine.Graphics.Camera;
import DabEngine.Graphics.Camera2D;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.Batch.SortType;
import DabEngine.Graphics.OpenGL.RenderTarget;
import DabEngine.Graphics.OpenGL.Light.Light;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.OpenGL.Viewport.Viewport;
import DabEngine.System.ComponentSystem;
import DabEngine.Utils.FixedArrayList;
import DabEngine.Utils.Pair;

public abstract class Scene {
	private LinkedHashSet<ComponentSystem> sys = new LinkedHashSet<>();
	public Camera camera;
	public ArrayDeque<Overlay> overlays = new ArrayDeque<>();
	public App app;
	protected FixedArrayList<Light> lights = new FixedArrayList<>(32);
	protected float ambientStrength;
	protected RenderTarget rt;
	public SortType sortingMode;
	public boolean paused;
	
	public Scene(App app, boolean renderToTexture){
		this.app = app;
		if(renderToTexture)
			rt = new RenderTarget(app.WIDTH, app.HEIGHT, app.vp);
	}
	
	public void render(Graphics g) {
		g.begin(rt != null ? rt : null, true);
			if(camera != null)
				g.setCamera(camera);
			if(!lights.isEmpty()){
				g.pushShader(Shaders.getUberShader("/Shaders/default.vs", "/Shaders/default.fs", new Pair<>("TEXTURED", "0"), new Pair<>("LIT", "0")));
				int i = 0;
				for(Light light : lights){
					Light.lightbuffer.bindToShader(g.getCurrentShader());
					Light.lightbuffer.put(0, light.toArray());
					Light.lightbuffer.put(1, ambientStrength);
					i++;
				}
			}
			for(ComponentSystem system : sys) {
				system.render(g);
			}
			if(g.getCurrentShader() == Shaders.getUberShader("/Shaders/default.vs", "/Shaders/default.fs", new Pair<>("TEXTURED", "0"), new Pair<>("LIT", "0")))
					g.popShader();
		g.end();
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
