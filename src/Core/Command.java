package Core;

import Graphics.Models.Renderable2D;

public interface Command {
	
	public void execute();
	public void execute(Renderable2D entity);
}
