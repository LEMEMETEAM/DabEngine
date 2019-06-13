package DabEngine.Scenes;

import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;

import DabEngine.Core.Engine;
import DabEngine.Entities.EntityManager;
import DabEngine.Graphics.Graphics;
import DabEngine.Input.InputHandler;
import DabEngine.States.State;
import DabEngine.System.ComponentSystem;
import DabEngine.Utils.Timer;

public abstract class SceneManager {
	
	private static Stack<Scene> sceneStack = new Stack<>();
	private  enum TransitionState implements State {
		TRANS_IN,
		CHANGE_SCENE,
		TRANS_OUT,
		END;
	}
	private static TransitionState transState;
	private static Stack<Transition> transitionStack = new Stack<>();
	private static Scene sceneToChangeTo;
	
	public static Scene getCurrentScene() {
		return sceneStack.peek();
	}
	
	public static void setCurrentScene(Scene newcurrentScene, Transition trans, boolean clearEntities) {
		if(clearEntities == true) EntityManager.clearAllEntities();
		InputHandler.INSTANCE.removeAll();
		if(trans != null) {
			transState = TransitionState.TRANS_IN;
			transitionStack.push(trans);
			sceneToChangeTo = newcurrentScene;
		}
		else {
			sceneStack.pop();
			sceneStack.push(newcurrentScene).init();
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

	static double timer = 0.0;
	public static void renderScenes(Graphics g){
		if(transState != TransitionState.END){
			timer += Timer.getDelta();
			switch(transState){
				case TRANS_IN:
					if(timer > transitionStack.peek().in_transition_time){
						transState = TransitionState.CHANGE_SCENE;
						timer = 0.0;
					}
					else{
						transitionStack.peek().in(g, timer);
					}

				case CHANGE_SCENE:
					sceneStack.pop();
					sceneStack.push(sceneToChangeTo);
					sceneToChangeTo = null;
					transState = TransitionState.TRANS_OUT;

				case TRANS_OUT:
					if(timer > transitionStack.peek().out_transition_time){
						transState = TransitionState.END;
						timer = 0.0;
					}
					else{
						transitionStack.peek().out(g, timer);
					}

				case END:
					transitionStack.peek().cleanUp();
					transitionStack.pop(); 
			}
		}
		for(Scene s : sceneStack){
			s.render(g);
		}
	}
}
