package DabEngine.States;

public interface State {

    boolean isInterruptable();
    boolean isFinished();
    void setFinished(boolean finished);
}
