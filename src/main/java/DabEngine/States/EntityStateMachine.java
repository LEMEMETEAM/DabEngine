package DabEngine.States;

import java.lang.ref.WeakReference;

import DabEngine.Entities.Entity;

public class EntityStateMachine {

    private WeakReference<Entity> entity;
    public EntityState currentState;
    
    public EntityStateMachine(Entity e){
        this.entity = new WeakReference<>(e);
    }

    public void changeState(EntityState s){
        if(currentState != s) {
            s.execute(entity.get());
            this.currentState = s;
        }
        else{
            throw new IllegalStateException("State already set to this");
        }
    }
}