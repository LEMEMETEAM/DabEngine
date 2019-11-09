package DabEngine;

import org.lwjgl.opengl.GL33;

import DabEngine.Core.App;
import DabEngine.Core.AppAdapter;
import DabEngine.Core.AppConfig;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.Viewport;
import DabEngine.Resources.ResourceManager;
import DabEngine.Script.ScriptManager;
import DabEngine.UI.UIContainer;
import DabEngine.Utils.Timer;

public class UITest extends AppAdapter
{

    private Graphics graphics;
    private Viewport vp;
    private Timer timer;
    private double time;
    private volatile int frames, updates;

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public void render() {
        // TODO Auto-generated method stub
        //GL33.glClearColor(1,1,1,1);
        GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);
        graphics.setMatrix(null);
        graphics.begin();
        ScriptManager.INSTANCE.invokeMethod("draw", graphics);
        graphics.end();
        frames++;
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        ScriptManager.INSTANCE.invokeMethod("update");
        updates++;
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        graphics = new Graphics(app);
        ResourceManager.init();
        vp = new Viewport(0, 0, app.getConfig().width, app.getConfig().height);
        vp.apply();

        timer = new Timer();
        time = timer.getTime();

        ScriptManager.INSTANCE.bind("app", app);
        ScriptManager.INSTANCE.execFile("C:/Users/B/Documents/DabEngine/src/test/java/DabEngine/UITest.lua");

        new Thread(() -> 
        {
            while(true)
            {
                if(timer.getTime() - time > 1.0)
                {
                    time++;
                    System.out.println("UPS:\t" + updates + "\tFPS:\t" + frames);
                    updates = 0;
                    frames = 0;
                }
            }
        }).start();
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        vp.update(width, height);
        vp.apply();
    }

    public static void main(String[] args) {
        AppConfig config = new AppConfig();
        config.width = 800;
        config.height = 600;
        new App(new UITest(), config).run();
    }
    
}