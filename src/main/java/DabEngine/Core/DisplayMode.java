package DabEngine.Core;

public class DisplayMode
{
    public Monitor monitor;
    public int width, height, refreshRate, bitsPerPixel;

    public DisplayMode(Monitor monitor, int width, int height, int refreshRate, int bitsPerPixel)
    {
        this.bitsPerPixel = bitsPerPixel;
        this.height = height;
        this.monitor = monitor;
        this.refreshRate = refreshRate;
        this.width = width;
    }
}