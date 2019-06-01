package DabEngine.Core;

import java.util.HashMap;

public abstract class App {
	
	protected int WIDTH, HEIGHT;
	protected String TITLE;
	protected HashMap<Integer, Integer> hints = new HashMap<>();
	protected boolean fullscreen;
	
	public abstract void render();
	public abstract void update();
	public abstract void init();
}
