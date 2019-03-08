package Scenes;

import java.util.ArrayList;

import System.System;

public abstract class SceneManager {
	
	private static Scene currentScene = null;
	public static final ArrayList<Scene> scenes = new ArrayList<>();
	private boolean in, out;
	
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
	
	public static void setCurrentScene(Scene newcurrentState) {
		setCurrentScene(newcurrentState, null);
	}
	
	public static void setCurrentScene(Scene newcurrentscene, Transition transition) {
		if(transition != null) {
			if(transition.in)
				transition.in();
			if(!transition.in && !transition.out) {
				currentScene = newcurrentscene;
				currentScene.init();
			}
			if(transition.out)
				transition.out();
		} else {
			currentScene = newcurrentscene;
			currentScene.init();
		}
	}
	
	public static void addSystemToAll(System... systems) {
		for(Scene scene : scenes) {
			for(System sys : systems) {
				scene.addSystem(sys);
			}
		}
	}
}
