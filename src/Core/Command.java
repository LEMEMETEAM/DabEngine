package Core;

import Entities.Entity;

public interface Command {
	
	public void execute();
	public void execute(Entity entity);
}
