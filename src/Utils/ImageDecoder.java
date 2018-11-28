package Utils;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ImageDecoder {

    private int width;
    private int height;
    private String filename;
    private ByteBuffer pixels;

    public ImageDecoder(String filename){
        this.filename = filename;
    }

    public ByteBuffer decode(){
        BufferedImage bi;

        try{
            bi = ImageIO.read(new File(filename));
            width = bi.getWidth();
            height = bi.getHeight();

            int[] pixels_raw = new int[width * height];
            pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);

            pixels = BufferUtils.createByteBuffer(width * height * 4);

            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    int pixel = pixels_raw[i * width + j];

                    pixels.put((byte) ((pixel >> 16) & 0xFF));  //R
                    pixels.put((byte) ((pixel >> 8) & 0xFF));   //G
                    pixels.put((byte) ((pixel) & 0xFF));        //B
                    pixels.put((byte) ((pixel >> 24) & 0xFF));  //A

                }
            }

            pixels.flip();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pixels;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
