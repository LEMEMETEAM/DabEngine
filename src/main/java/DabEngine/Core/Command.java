package DabEngine.Core;

import DabEngine.Entities.Entity;

public interface Command {
	
	public void execute(Entity entity);
}
