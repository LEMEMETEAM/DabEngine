package DabEngine.States;

public class EntityStateMachine {

    private int entity;
    public EntityState currentState;
    
    public EntityStateMachine(int e){
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