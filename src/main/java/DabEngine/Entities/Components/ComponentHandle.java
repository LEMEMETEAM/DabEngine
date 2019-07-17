package DabEngine.Entities.Components;

import java.lang.reflect.Array;

import org.python.bouncycastle.util.Arrays;

public class ComponentHandle<T extends Component> {

    public T[] comps;
    public Class<T> type;

    public ComponentHandle(Class<T> type){
        comps = (T[])Array.newInstance(type, 64);
        this.type = type;
    }

    public void assign(int entity, T comp){
        if(entity >= comps.length){
            resize();
        }
        T c = comps[entity];
        if(c == null){
            comps[entity] = comp;
        }
        c = null;
    }

    public void resize(){
        int oldsize = comps.length;
        int newsize = oldsize + oldsize/2;
        T[] newarray = (T[])Array.newInstance(type, newsize);
        System.arraycopy(comps, 0, newarray, 0, comps.length);
        comps = newarray;
    }

    public T component(int entity){
        return (T)comps[entity];
    }

    public boolean has(int entity){
        return comps[entity] != null;
    }

    @Override
    public boolean equals(Object obj) {
        return type != ((ComponentHandle<T>)obj).type;
    }
}