package Core;

import Entities.GameObject;

public interface Command {
	
	public void execute();
	public void execute(GameObject entity);
}
