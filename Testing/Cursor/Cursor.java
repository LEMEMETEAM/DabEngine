package Cursor;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Cursor{

    private long cursorid;

    public Cursor(){
        try {
            BufferedImage img = ImageIO.read(new FileInputStream(new File("./res/cursorimg.png")));

            int width = img.getWidth();
            int height = img.getHeight();

            int[] pixels = new int[width * height];
            img.getRGB(0, 0, width, height, pixels, 0, width);

            // convert image to RGBA format
            ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    int pixel = pixels[y * width + x];

                    buffer.put((byte) ((pixel >> 16) & 0xFF));  // red
                    buffer.put((byte) ((pixel >> 8) & 0xFF));   // green
                    buffer.put((byte) (pixel & 0xFF));          // blue
                    buffer.put((byte) ((pixel >> 24) & 0xFF));  // alpha
                }
            }
            buffer.flip(); // this will flip the cursor image vertically

            // create a GLFWImage
            GLFWImage cursorImg= GLFWImage.create();
            cursorImg.width(width);     // setup the images' width
            cursorImg.height(height);   // setup the images' height
            cursorImg.pixels(buffer);   // pass image data

            // create custom cursor and store its ID
            int hotspotX = cursorImg.width()/2;
            int hotspotY = cursorImg.height()/2;
            cursorid = GLFW.glfwCreateCursor(cursorImg, hotspotX , hotspotY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getCursorid(){
        return cursorid;
    }
}
