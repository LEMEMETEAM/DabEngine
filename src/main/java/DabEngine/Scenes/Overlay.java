package DabEngine.Scenes;

import DabEngine.Graphics.Graphics;

public abstract class Overlay {
    private Scene scene;

    public void addedToScene(Scene s){
        this.scene = scene;
    }

    public abstract void update();
    public abstract void render(Graphics g);
}