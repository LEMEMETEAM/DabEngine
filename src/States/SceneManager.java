package States;

import java.util.ArrayList;

import javax.swing.JFrame;

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
	
	public static void setCurrentState(Scene newcurrentState) {
		setCurrentScene(newcurrentState, null);
	}
	
	public static void initScenes() {
		for(Scene scene : scenes) {
			scene.init();
		}
	}
	
	public static void setCurrentScene(Scene newcurrentscene, Transition transition) {
		if(transition != null) {
			transition.in();
			currentScene = newcurrentscene;
			transition.out();
		} else {
			currentScene = newcurrentscene;
		}
	}
}
