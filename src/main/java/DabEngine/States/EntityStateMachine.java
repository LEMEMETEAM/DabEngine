package DabEngine.States;

import DabEngine.Entities.Entity;

public class EntityStateMachine {

    private Entity entity;
    public EntityState currentState;
    
    public EntityStateMachine(Entity e){
        this.entity = e;
    }

    public void changeState(EntityState s){
        if(currentState != s) {
            s.execute(entity);
            this.currentState = s;
        }
        else{
            throw new IllegalStateException("State already set to this");
        }
    }
}