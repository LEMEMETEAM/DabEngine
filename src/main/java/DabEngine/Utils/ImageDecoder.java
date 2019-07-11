package DabEngine.Utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

/*
 * ONLY WORKS WITH FILE, iNPUTsTREAM AND BUFFEREDIMAGE
 */
public class ImageDecoder<T> {

    private int width;
    private int height;
    private ByteBuffer pixels;
    private T resource;

    public ImageDecoder(T resource){
        this.resource = resource;
    }

    public ByteBuffer decode() throws IOException{
        BufferedImage bi = null;
        
    	if(resource instanceof File) {
    		bi = ImageIO.read((File)resource);
    	}
    	else if(resource instanceof InputStream) {
    		bi = ImageIO.read((InputStream)resource);
    	}
    	else if(resource instanceof BufferedImage) {
    		bi = (BufferedImage)resource;
    	}
    	else {
    		throw new IOException("Not Correct Type");
    	}
    	
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

        return pixels;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void close() throws IOException {
        pixels.clear();
        if(resource instanceof InputStream){
            ((InputStream)resource).close();
        }
        else if(resource instanceof BufferedImage){
            ((BufferedImage)resource).flush();
        }
    }
}
