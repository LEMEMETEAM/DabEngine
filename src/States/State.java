package States;

import Core.Engine;
import Graphics.*;
import Input.InputHandler;

public abstract class State {
	public abstract void render(Engine engine, SpriteBatch batch, Camera cam);
	public abstract void update(Engine engine, InputHandler handler, Camera cam);
}
