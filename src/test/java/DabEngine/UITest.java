package DabEngine;

import org.lwjgl.opengl.GL33;

import DabEngine.Core.App;
import DabEngine.Core.Engine;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.OpenGL.Viewport.Viewport;
import DabEngine.Resources.ResourceManager;
import DabEngine.Resources.Font.Font;
import DabEngine.UI.UIButton;
import DabEngine.UI.UIContainer;

public class UITest extends App
{

    private static Engine engine = new Engine();
    private Graphics graphics;
    private UIContainer container;

    {
        TITLE = "UITest";
        WIDTH = 800;
        HEIGHT = 600;
        MAXIMISED = false;
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public void render() {
        // TODO Auto-generated method stub
        GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);
        graphics.setMatrix(null);
        graphics.begin();
        container.draw(graphics);
        graphics.end();
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        container.update();
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        graphics = engine.createGraphics();
        ResourceManager.init();
        vp = new Viewport(0, 0, WIDTH, HEIGHT);
        vp.apply();

        //UIContainer.DEBUG = true;

        container = new UIContainer(WIDTH/2, HEIGHT/2, WIDTH, HEIGHT);
        UIButton button = new UIButton(0, 0, 100, 50, "press me");
        container.addElement(button);
        button.setCallback(() -> System.out.println("test"));
        button.setFont(ResourceManager.INSTANCE.getFont("C:/Windows/Fonts/arial.ttf", 24, 2));
        button.setAnchor(-0.5f, -0.5f);
        //button.setSize(200, 50);

        container.observe(engine.getMouse(), engine.getKeyboard());
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    public static void main(String[] args) {
        engine.init(new UITest());
        engine.run();
    }
    
}