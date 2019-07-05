package DabEngine.Utils;

public class Color {

    private float[] color = new float[4 * 4];

    public Color(){ }

    public Color(float[] c1){
        for(int i = 0; i < 4*4; i++){
            color[i] = c1[i % 4];
        }
    }

    public Color setAlpha(float alpha){
        color[3] = alpha;
        color[6] = alpha;
        color[12] = alpha;
        color[16] = alpha;
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

    public static final Color RED = new Color(new float[]{1,0,0,1});
    public static final Color BLUE = new Color(new float[]{0,0,1,1});
    public static final Color GREEN = new Color(new float[]{0,1,0,1});
    public static final Color BLACK = new Color(new float[]{0,0,0,1});
    public static final Color WHITE = new Color(new float[]{1,1,1,1});
}