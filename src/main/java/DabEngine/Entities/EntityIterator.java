package DabEngine.Entities;

import DabEngine.Entities.Components.Component;
import DabEngine.Entities.Components.ComponentHandle;

/**
 * Functional Interface that only takes 1 argumenet but returns nothing.
 */
public interface EntityIterator {
    public void each(int entity);
}