package DabEngine.Scenes;

import DabEngine.Core.App;
import DabEngine.Graphics.Graphics;

public class TransitionScene extends Scene {

    private IScene oldScene, newScene;
    private Transition outEffect, inEffect;
    private enum TransitionState {IN, OUT}
    private TransitionState transitionState;

    public TransitionScene(App app, IScene oldScene, IScene newScene, Transition outEffect, Transition inEffect){
        super(app);
        this.oldScene = oldScene;
        this.newScene = newScene;
        this.outEffect = outEffect;
        this.inEffect = inEffect;
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        switch(transitionState){
            case OUT:
                if(outEffect != null){
                    if(oldScene != null){
                        oldScene.render(g);
                    }
                    outEffect.render(g);
                    if(outEffect.isFinished()){
                        transitionState = TransitionState.IN;
                    }
                } else {
                    transitionState = TransitionState.IN;
                }
                break;
            case IN:
                if(inEffect != null){
                    if(newScene != null){
                        newScene.render(g);
                    }
                    inEffect.render(g);
                    if(inEffect.isFinished()){
                        SceneManager.setScene(newScene);
                    }
                } else {
                    SceneManager.setScene(newScene);
                }
                break;
        }
    }
}