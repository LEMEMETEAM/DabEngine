package DabEngine.Entities;

import DabEngine.Entities.Components.Component;
import DabEngine.Entities.Components.ComponentHandle;

public interface EntityIterator {
    public void each(int entity);
}