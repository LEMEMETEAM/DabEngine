package DabEngine.Entities;

import java.util.function.Predicate;

public class EntityFilter {


    public static Predicate<Integer> boolFunc;
    

    public static EntityFilter has(Class type){
        boolFunc = e -> EntityManager.INSTANCE.has(e, type);
        return null;
    }

    public static EntityFilter and(Class type){
        boolFunc = boolFunc.and(e -> EntityManager.INSTANCE.has(e, type));
        return null;
    }

    public static EntityFilter or(Class type){
        boolFunc = boolFunc.or(e -> EntityManager.INSTANCE.has(e, type));
        return null;
    }
}