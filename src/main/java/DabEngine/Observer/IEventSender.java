package DabEngine.Observer;

import java.util.ArrayList;
import java.util.WeakHashMap;

public abstract class IEventSender<T extends IEventListener> {

    protected ArrayList<T> observers = new ArrayList<>();

    public void addObserver(T listener){
        observers.add(listener);
    }

    public void removeObserver(T listener){
        observers.remove(listener);
    }

    public void removeAll(){
        observers.clear();
    }
} 