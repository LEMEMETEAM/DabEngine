package DabEngine.Scenes;

public abstract class SceneManager {

	private static IScene currentScene;
	
	public static void switchScene(IScene scene, Transition out, Transition in){
		currentScene = new TransitionScene(((Scene)scene).app, currentScene, scene, out, in);
	}

	public static void setScene(IScene scene){
		currentScene = scene;
		scene.init();
	}

	public static IScene getScene(){
		return currentScene;
	}
	
}
