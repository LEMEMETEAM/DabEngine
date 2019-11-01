package DabEngine.Scenes;

import java.util.ArrayDeque;

import DabEngine.Core.AppAdapter;
import DabEngine.Graphics.Graphics;
import DabEngine.States.StateManager;
import DabEngine.System.ComponentSystem;

public class Scene implements IScene {

    protected StateManager stateManager = new StateManager();
    /*Must be used as a stack, not linked list!*/
    protected ArrayDeque<ComponentSystem> systems = new ArrayDeque<ComponentSystem>(){
        @Override
        public void addFirst(ComponentSystem e){
            e.addedToScene(Scene.this);
            super.addFirst(e);
        }

        @Override
        public ComponentSystem removeFirst(){
            ComponentSystem c = super.removeFirst();
            c.dispose();
            return c;
        }
    };
    protected AppAdapter app;

    protected Scene(AppAdapter app){
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