package DabEngine.Core;

public class Monitor {

    public long monitor;
    public int virtualX, virtualY;
    public String name;

    public Monitor(long monitor, int virtualX, int virtualY, String name)
    {
        this.monitor = monitor;
        this.name = name;
        this.virtualY = virtualY;
        this.virtualX = virtualX;
    }
    
}