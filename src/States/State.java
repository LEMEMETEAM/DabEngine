package States;

import Core.Engine;
import Graphics.Camera;
import Graphics.SpriteBatch;
import Input.InputHandler;

public interface State {
	public void render(Engine engine, SpriteBatch batch, Camera cam);
	public void update(Engine engine, Camera cam);
	public void processInput(Engine engine, InputHandler handler, Camera cam);
}
