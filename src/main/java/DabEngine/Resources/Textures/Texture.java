package DabEngine.Resources.Textures;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_loadf;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.internal.MemUtil;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import DabEngine.Resources.Resource;

/**
 * Essentially just a handle for the texture id, you can create one from a byte
 * buffer and a you can create a empty/blnk texture for framebuffers and other
 * tings.
 */
public class Texture extends Resource {

    private int t_id, width, height, numChannels, unitBackup;
    private boolean mipmap, hdr, created;
    private Buffer pixels;

    public Texture(String filename, boolean mipmap, boolean hdr) 
    {
        super(filename);
        t_id = 0;

        width = 1;
        height = 1;

        numChannels = 4;
        this.mipmap = mipmap;
        this.hdr = hdr;

        created = false;
    }

    public Texture(int width, int height, boolean mipmap, boolean hdr) 
    {
        super();
        t_id = 0;

        this.width = width;
        this.height = height;

        numChannels = 4;
        this.mipmap = mipmap;
        this.hdr = hdr;

        if(hdr) pixels = MemoryUtil.memCallocFloat(numChannels*width*height);
        else pixels = MemoryUtil.memCalloc(numChannels*width*height);
        for(int i = 0; i < width*height; i++)
        {
            if(hdr)
            {
                FloatBuffer fp = ((FloatBuffer)pixels);
                fp.put((byte)255);
                fp.put((byte)255);
                fp.put((byte)255);
                fp.put((byte)255);
            }
            else
            {
                ByteBuffer fp = ((ByteBuffer)pixels);
                fp.put((byte)255);
                fp.put((byte)255);
                fp.put((byte)255);
                fp.put((byte)255);
            }
        }

        created = true;
    }

    public Texture(int id, int width, int height, boolean mipmap, boolean hdr){
        super();
        t_id = id;
        this.width = width;
        this.height = height;

        numChannels = 4;
        this.mipmap = mipmap;
        this.hdr = hdr;

        ready = true;
        created = true;
    }

    @Override
    protected void create() {
        // TODO Auto-generated method stub

        if (t_id != 0 || ready)
            return;

        if(!created)    ready = loadRaw();

        t_id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, t_id);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, mipmap ? GL_LINEAR_MIPMAP_LINEAR : GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE); 

        if(hdr)
            glTexImage2D(GL_TEXTURE_2D, 0, numChannels == 4 ? GL_RGBA16F : (numChannels == 3 ? GL_RGB16F : (numChannels == 1 ? GL_LUMINANCE16 : GL_RGBA16F)), width, height, 0, numChannels == 4 ? GL_RGBA16F : (numChannels == 3 ? GL_RGB16F : (numChannels == 1 ? GL_LUMINANCE : GL_RGBA16F)), GL_FLOAT, (FloatBuffer)pixels.flip());
        else
            glTexImage2D(GL_TEXTURE_2D, 0, numChannels == 4 ? GL_RGBA : (numChannels == 3 ? GL_RGB : (numChannels == 1 ? GL_LUMINANCE : GL_RGBA)), width, height, 0, numChannels == 4 ? GL_RGBA : (numChannels == 3 ? GL_RGB : (numChannels == 1 ? GL_LUMINANCE : GL_RGBA)), GL_UNSIGNED_BYTE, (ByteBuffer)pixels.flip());

        if(mipmap)
        {
            glGenerateMipmap(GL_TEXTURE_2D);
        }

        if(created) ready = true;
        
    }

    private boolean loadRaw()
    {
        if(filename != null || !filename.isEmpty())
        {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer x = stack.callocInt(1);
                IntBuffer y = stack.callocInt(1);
                IntBuffer channels_in_file = stack.callocInt(1);
                pixels = hdr ? stbi_loadf(filename, x, y, channels_in_file, 0) : stbi_load(filename, x, y, channels_in_file, 0);
                if(pixels == null)
                {
                    return false;
                }

                width = x.get(0);
                height = y.get(0);
                numChannels = channels_in_file.get(0);

            }
        }

        return true;
    }

    @Override
    public void dispose() 
    {
        // TODO Auto-generated method stub
        if(t_id != 0)
        {
            glDeleteTextures(t_id);
            t_id = 0;
        }

        if(pixels != null) pixels.clear();
    }

    public void bind(int unit)
    {

        if(!ready) return;

        unitBackup = unit;

        glActiveTexture(GL_TEXTURE0 + unit);

        glBindTexture(GL_TEXTURE_2D, t_id);
    }

    public void unbind()
    {

        if(!ready) return;

        glActiveTexture(GL_TEXTURE0 + unitBackup);

        glBindTexture(GL_TEXTURE_2D, 0);

        if(unitBackup != 0)
        {
            glActiveTexture(GL_TEXTURE0);
        }
    }

    public enum TextureFilter 
    {
        LINEAR,
        NEAREST,
        MIPMAP,
        NEAREST_LINEAR;
    }

    public enum TextureWrap
    {
        REPEAT,
        MIRROR,
        CLAMP_TO_EDGE,
        CLAMP_TO_BORDER
    }

    public void setFilterMode(TextureFilter filter)
    {
        if(!ready) return;

        bind(0);
        switch(filter)
        {
            case LINEAR:
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                break;
            case NEAREST:
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
                break;
            case MIPMAP:
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                break;
            case NEAREST_LINEAR:
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                break;
        }
        unbind();
    }

    public void setWrapMode(TextureWrap wrap)
    {

        if(!ready) return;
        bind(0);

        switch(wrap)
        {
            case REPEAT:
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
                break;
            case MIRROR:
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_MIRRORED_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_MIRRORED_REPEAT);
                break;
            case CLAMP_TO_EDGE:
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
                break;
            case CLAMP_TO_BORDER:
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
                break;
        }
        unbind();

    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the pixels
     */
    public Buffer getPixels() {
        return pixels;
    }

    /**
     * @return the t_id
     */
    public int getID() {
        return t_id;
    }


}
