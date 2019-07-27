package DabEngine.Scenes;

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
import DabEngine.Graphics.OpenGL.Light.Light2D;
import DabEngine.Graphics.OpenGL.Viewport.Viewport;
import DabEngine.System.ComponentSystem;
import DabEngine.Utils.FixedArrayList;

public abstract class Scene {
	private LinkedHashSet<ComponentSystem> sys = new LinkedHashSet<>();
	public Camera camera;
	public Stack<Scene> overlays = new Stack<>();
	public App app;
	protected FixedArrayList<Light2D> lights = new FixedArrayList<>(32);
	protected float ambientStrength;
	protected RenderTarget rt;
	public SortType sortingMode;
	
	public Scene(App app, boolean renderToTexture){
		this.app = app;
		if(renderToTexture)
			rt = new RenderTarget(app.WIDTH, app.HEIGHT, app.vp);
	}
	
	public void render(Graphics g) {
		g.begin(rt != null ? rt : null);
			if(camera != null)
				g.setCamera(camera);
			if(!lights.isEmpty()){
				g.pushShader(Light2D.LIGHT_SHADER);
				int i = 0;
				for(Light2D light : lights){
					g.getCurrentShader().setUniform("lights["+i+"].position", light.pos);
					g.getCurrentShader().setUniform("lights["+i+"].color", light.color);
					g.getCurrentShader().setUniform("ambientStrength", ambientStrength);
					i++;
				}
			}
			else{
				if(g.getCurrentShader() == Light2D.LIGHT_SHADER)
					g.popShader();
			}
			for(ComponentSystem system : sys) {
				system.render(g);
			}
		g.end();
		for(Scene s : overlays){
			s.render(g);
		}
	}

	public void tick() {
		for(ComponentSystem system : sys) {
			system.update();
		}
		for(Scene s : overlays){
			s.tick();
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

	public void addOverlay(Scene overlay){
		overlays.push(overlay).init();
	}
	
	public abstract void init();
}
