package DabEngine.Utils;

public class Color {

    public float[] color = new float[4];

    public Color(){ }

    public Color(float r, float g, float b, float a){
        color[0] = r;
        color[1] = g;
        color[2] = b;
        color[3] = a;
    }

    public Color(Color c)
    {
        this(c.color[0], c.color[1], c.color[2], c.color[3]);
    }

    public Color setAlpha(float alpha){
        color[3] = alpha;
        return this;
    }

    /**
     * @return the color
     */
    public float[] getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(float[] color) {
        this.color = color;
    }

    public void lighten(float percent)
    {
        color[0] += (percent * color[0]);
        color[1] += (percent * color[1]);
        color[2] += (percent * color[2]);
        color[3] += (percent * color[3]);
    }

    public void darken(float percent)
    {
        color[0] -= (percent * color[0]);
        color[1] -= (percent * color[1]);
        color[2] -= (percent * color[2]);
        color[3] -= (percent * color[3]); 
    }

    public static final Color RED = new Color(1,0,0,1);
    public static final Color BLUE = new Color(0,0,1,1);
    public static final Color GREEN = new Color(0,1,0,1);
    public static final Color BLACK = new Color(0,0,0,1);
    public static final Color WHITE = new Color(1,1,1,1);
}