package DabEngine.Scenes;

import java.util.ArrayDeque;

import DabEngine.Core.App;
import DabEngine.Graphics.Graphics;
import DabEngine.States.StateManager;
import DabEngine.System.ComponentSystem;

public class Scene implements IScene {

    protected StateManager stateManager = new StateManager();
    protected ArrayDeque<ComponentSystem> systems = new ArrayDeque<>();
    protected App app;

    protected Scene(App app){
        this.app = app;
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        for(ComponentSystem s : systems){
            s.render(g);
        }
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        for(ComponentSystem s : systems){
            s.update();
        }
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resize() {
        // TODO Auto-generated method stub

    }

}