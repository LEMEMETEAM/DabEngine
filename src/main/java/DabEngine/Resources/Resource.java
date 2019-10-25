package DabEngine.Resources;

import DabEngine.Core.IDisposable;

public abstract class Resource implements IDisposable {

    protected boolean ready;
    protected String filename;

    public Resource(String filename)
    {
        this.filename = filename;
        ready = false;
    }

    public Resource()
    {
        ready = false;
    }

    public void load()
    {
        create();
    }

    public void free()
    {
        dispose();
        ready = false;
    }

    protected abstract void create();

    @Override
    public abstract void dispose();
}