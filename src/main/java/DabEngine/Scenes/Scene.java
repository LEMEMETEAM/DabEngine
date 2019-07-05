package DabEngine.Scenes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Stack;

import DabEngine.Core.App;
import DabEngine.Graphics.Camera;
import DabEngine.Graphics.Camera2D;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.ProjectionMatrix;
import DabEngine.Graphics.OpenGL.RenderTarget;
import DabEngine.Graphics.OpenGL.Light.Light2D;
import DabEngine.Graphics.OpenGL.Viewport.Viewport;
import DabEngine.System.ComponentSystem;
import DabEngine.Utils.FixedArrayList;

public abstract class Scene {
	private HashSet<ComponentSystem> sys = new HashSet<>();
	public Camera camera;
	public Stack<Scene> overlays = new Stack<>();
	public App app;
	private FixedArrayList<Light2D> lights = new FixedArrayList<>(32);
	private float ambientStrength;
	private RenderTarget rt;
	
	public Scene(App app, Viewport vp, boolean renderToTexture){
		this.app = app;
		rt = new RenderTarget(app.WIDTH, app.HEIGHT, vp);
	}
	
	public void render(Graphics g) {
		g.begin(rt != null ? rt : null);
			
			if(!lights.isEmpty()){
				g.pushShader(Light2D.LIGHT_SHADER);
				for(int i = 0; i < 32; i++){
					g.getCurrentShader().setUniform("lights["+i+"].position", lights.get(i).pos);
					g.getCurrentShader().setUniform("lights["+i+"].color", lights.get(i).color);
					g.getCurrentShader().setUniform("ambientStrength", ambientStrength);
				}
			}
			else{
				g.popShader();
			}
			for(ComponentSystem system : sys) {
				system.render(g);
			}
			for(Scene s : overlays){
				s.render(g);
			}
		g.end();
	}
	public void tick() {
		if(camera != null){
			ProjectionMatrix.addViewMatrix(camera.getProjection());
		}
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
