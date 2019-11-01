package DabEngine.Core;

public abstract class AppAdapter implements IDisposable
{
    public App app;
    public abstract void init();
    public abstract void render();
    public abstract void update();
    public abstract void resize(int width, int height);
    public void connectApp(App app)
    {
        this.app = app;
    }
}