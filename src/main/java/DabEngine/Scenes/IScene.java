package DabEngine.Scenes;

import DabEngine.Graphics.Graphics;

public interface IScene {

	public void render(Graphics g);
	public void update();
	public void init();
	public void resize();
	
}
