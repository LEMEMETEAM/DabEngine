package DabEngine.Graphics.Models;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA16;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import DabEngine.Utils.ImageDecoder;

public class Texture implements Cloneable {

    private int t_id;
    private ImageDecoder<?> decoder;
    public int width;
    public int height;
    private TextureRegion region;

    public Texture(InputStream stream, int tileNomX, int tileNomY) throws IOException {
        decoder = new ImageDecoder<InputStream>(stream);

        ByteBuffer pixels = decoder.decode();
        width = decoder.getWidth();
        height = decoder.getHeight();

        t_id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, t_id);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        region = new TextureRegion(tileNomX, tileNomY);
    }

    public Texture(File file, int tileNomX, int tileNomY) throws IOException {
        decoder = new ImageDecoder<File>(file);

        ByteBuffer pixels = decoder.decode();
        width = decoder.getWidth();
        height = decoder.getHeight();

        t_id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, t_id);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        region = new TextureRegion(tileNomX, tileNomY);
    }

    public Texture(BufferedImage img, int tileNomX, int tileNomY) throws IOException {
        decoder = new ImageDecoder<BufferedImage>(img);

        ByteBuffer pixels = decoder.decode();
        width = decoder.getWidth();
        height = decoder.getHeight();

        t_id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, t_id);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        region = new TextureRegion(tileNomX, tileNomY);
    }

    public Texture(File file) throws IOException {
        this(file, 1, 1);
    }

    public Texture(InputStream stream) throws IOException {
        this(stream, 1, 1);
    }

    public Texture(BufferedImage img) throws IOException {
        this(img, 1, 1);
    }

    public TextureRegion getRegion() {
        return region;
    }

    public Texture setRegion(int tileNomX, int tileNomY) {
        region = new TextureRegion(tileNomX, tileNomY);
        return this;
    }

    public void bind(int texture_sample) {
        if(texture_sample > 32){
            throw new IllegalStateException("Must be between 0 and 31");
        }
        glActiveTexture(GL_TEXTURE0 + texture_sample);
        glBindTexture(GL_TEXTURE_2D, t_id);
    }
}
