package DabEngine.Graphics.OpenGL.Textures;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import DabEngine.Utils.ImageDecoder;

public class TextureLoader {

    private ImageDecoder<?> decoder;
    public int width;
    public int height;
    public ByteBuffer pixels;

    public TextureLoader(InputStream stream) throws IOException {
        decoder = new ImageDecoder<InputStream>(stream);

        ByteBuffer pixels = decoder.decode();
        width = decoder.getWidth();
        height = decoder.getHeight();

        this.pixels = pixels;

    }

    public TextureLoader(File file) throws IOException {
        decoder = new ImageDecoder<File>(file);

        ByteBuffer pixels = decoder.decode();
        width = decoder.getWidth();
        height = decoder.getHeight();

        this.pixels = pixels;
    }

    public TextureLoader(BufferedImage img) throws IOException {
        decoder = new ImageDecoder<BufferedImage>(img);

        ByteBuffer pixels = decoder.decode();
        width = decoder.getWidth();
        height = decoder.getHeight();

        this.pixels = pixels;
    }
}