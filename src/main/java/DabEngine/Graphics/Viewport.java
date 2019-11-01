package DabEngine.Graphics;

import org.lwjgl.opengl.GL11;

public class Viewport {
    public int x, y, width, height;
    private float aspectRatio;

    public Viewport(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.aspectRatio = (float)height/width;
    }

    public void apply(){
        GL11.glViewport(x, y, width, height);
    }

    public void update(int newWidth, int newHeight){
        float newRatio = (float)newHeight/newWidth;
        float scale = newRatio > aspectRatio ? (float)newWidth / width : (float)newHeight / height;

        //center
        x = (newWidth - Math.round(width * scale))/2;
        y = (newHeight - Math.round(height * scale))/2;
        width *= scale;
        height *= scale;
    }
}