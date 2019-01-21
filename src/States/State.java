package States;

import Core.Engine;
import Graphics.*;
import Input.InputHandler;

public abstract class State extends Container2D {
	public abstract void render(Engine engine, SpriteBatch batch, Camera cam);
	public abstract void update(Engine engine, Camera cam);
	public abstract void processInput(Engine engine, InputHandler handler, Camera cam);
}
