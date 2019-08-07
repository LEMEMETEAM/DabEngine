package DabEngine.Scenes;

import DabEngine.Graphics.Graphics;

public abstract class Overlay {
    protected Scene scene;

    public Overlay addedToScene(Scene s){
        this.scene = s;
        return this;
    }

    public abstract void update();
    public abstract void render(Graphics g);
}