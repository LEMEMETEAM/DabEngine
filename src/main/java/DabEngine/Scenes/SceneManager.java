package DabEngine.Scenes;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import DabEngine.Core.Engine;
import DabEngine.Entities.EntityManager;
import DabEngine.Input.InputHandler;
import DabEngine.System.ComponentSystem;
import DabEngine.Utils.Timer;

public abstract class SceneManager {
	
	private static Scene currentScene = null;
	public static final ArrayList<Scene> scenes = new ArrayList<>();
	
	public static Scene getCurrentScene() {
		return currentScene;
	}
	
	public static void addScene(Scene scene) {
		if(!scenes.contains(scene)) {
			scenes.add(scene);
		}
	}
	
	public static <T> T getScene(Class<T> clazz) {
		for(Scene scene : scenes) {
			if(clazz.isAssignableFrom(scene.getClass())) {
				return clazz.cast(scene);
			}
		}
		return null;
	}
	
	public static void setCurrentScene(Scene newcurrentScene, Transition trans, boolean clearEntities) {
		if(clearEntities == true) EntityManager.clearAllEntities();
		InputHandler.INSTANCE.removeAll();
		Timer timer = new Timer();
		if(trans != null) {
			CompletableFuture.runAsync(() -> {
				timer.start();
				trans.initOut();
				while(timer.counter <= trans.out_transition_time * Engine.TARGET_FPS) {
					trans.out(timer.counter);
				}
				timer.stop();
			}).thenRun(() -> {
				currentScene = newcurrentScene;
				currentScene.init();
			}).thenRun(() -> {
				timer.start();
				trans.initIn();
				while(timer.counter <= trans.in_transition_time * Engine.TARGET_FPS) {
					trans.in(timer.counter);
				}
				timer.stop();
			}).thenRun(() -> trans.cleanUp());
		}
		else {
			currentScene = newcurrentScene;
			currentScene.init();
		}
	}
	
	public static void setCurrentScene(Scene newcurrentScene, Transition trans) {
		setCurrentScene(newcurrentScene, trans, false);
	}
	
	public static void setCurrentScene(Scene newcurrentScene, boolean clearEntities) {
		setCurrentScene(newcurrentScene, null, clearEntities);
	}
	
	public static void setCurrentScene(Scene newcurrentScene) {
		setCurrentScene(newcurrentScene, null, false);
	}
	
	
	public static void addSystemToAll(ComponentSystem... systems) {
		for(Scene scene : scenes) {
			for(ComponentSystem sys : systems) {
				scene.addSystem(sys);
			}
		}
	}
}
