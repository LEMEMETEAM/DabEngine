package DabEngine.Entities.Components;

import java.lang.reflect.Array;

/**
 * Handle for a component that maps entities to components of this type.
 * @param <T> component type
 */
public class ComponentHandle<T extends Component> {

    public T[] comps;
    public Class<T> type;

    public ComponentHandle(Class<T> type){
        comps = (T[])Array.newInstance(type, 64);
        this.type = type;
    }

    /**
     * Puts a component at index of entity in comps array. Allows for fast retrieval of components for entity.
     * @param entity the entity id
     * @param comp the component to assign to entity
     */
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

    /**
     * resizes the comps array to accomodate for more entities
     */
    public void resize(){
        int oldsize = comps.length;
        int newsize = oldsize + oldsize/2;
        T[] newarray = (T[])Array.newInstance(type, newsize);
        System.arraycopy(comps, 0, newarray, 0, comps.length);
        comps = newarray;
    }

    /**
     * retries a component for the specified entity
     * @param entity the entity id
     * @return the component
     */
    public T component(int entity){
        return (T)comps[entity];
    }

    /**
     * check if entity has component of this type
     * @param entity the entity id
     * @return whether the entity has component or not
     */
    public boolean has(int entity){
        return comps[entity] != null;
    }
}