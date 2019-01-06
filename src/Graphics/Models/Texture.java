package Graphics.Models;

import Utils.ImageDecoder;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Texture {

    private int t_id;
    private ImageDecoder decoder;
    private int width;
    private int height;

    public Texture(String filename){
        decoder = new ImageDecoder(filename);

        ByteBuffer pixels = decoder.decode();
        width = decoder.getWidth();
        height = decoder.getHeight();

        t_id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, t_id);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
    }

    public void bind(){
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, t_id);
    }
}
