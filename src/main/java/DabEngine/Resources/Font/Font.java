package DabEngine.Resources.Font;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.joml.internal.MemUtil;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import DabEngine.Resources.Resource;
import DabEngine.Resources.Textures.Texture;
import DabEngine.Utils.Utils;

/**
 * class that creates a texture atlas from a given TrueType file.
 */
public class Font extends Resource 
{

    private STBTTPackedchar.Buffer cData;
    private Texture texture;
    public float size;
    private int ascent, descent, lineGap;

    private STBTTFontinfo info;
    private ByteBuffer data;

    private int oversampling;
	public boolean align_to_int;

    /**
     * creates the texture atlas for the specified ttf file.
     * @param fontFile the file path of the font
     * @param size the size of the glyphs
     * @param oversampling oversampling value
     */
    public Font(String fontFile, float size, int oversampling) {
        super(fontFile);

        this.size = size;

        texture = null;
        cData = null;

        this.oversampling = oversampling;
    }

    /**
     * Gets the texture atlas for this font
     * @return the texture
     */
    public Texture getTexture(){
        return texture;
    }

    @Override
    protected void create() {
        // TODO Auto-generated method stub
        if(ready || texture != null) return;

        int t_id = glGenTextures();

        info = STBTTFontinfo.create();

        cData = STBTTPackedchar.malloc(128);
        try {
            data = Utils.ioResourceToByteBuffer(filename, 512 * 1024);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ready = false;
        }
        ByteBuffer bitmap = MemoryUtil.memCalloc(512*512);

        if(!stbtt_InitFont(info, data))
        {
            ready = false;
        }

        try(MemoryStack stack = MemoryStack.stackPush())
        {
            IntBuffer 
                a = stack.mallocInt(1),
                d = stack.mallocInt(1),
                l = stack.mallocInt(1);
            
            stbtt_GetFontVMetrics(info, a, d, l);

            ascent = a.get(0);
            descent = d.get(0);
            lineGap = d.get(0);
        }

        STBTTPackContext pc = STBTTPackContext.malloc();
        stbtt_PackBegin(pc, bitmap, 512, 512, 0, 1, NULL);
            cData.limit(127);
            cData.position(32);
            stbtt_PackSetOversampling(pc, oversampling, 1);
            stbtt_PackFontRange(pc, data, 0, size, 32, cData);
        cData.clear();
        stbtt_PackEnd(pc);

        ByteBuffer bitmapRGBA = MemoryUtil.memCalloc(512*512*4);
        for(int i = 0; i < 512 * 512; i++) {
            bitmapRGBA.put((byte)255);
            bitmapRGBA.put((byte)255);
            bitmapRGBA.put((byte)255);
            bitmapRGBA.put(bitmap.get(i));
        }
        bitmapRGBA.flip();
        MemoryUtil.memFree(bitmap);

        glBindTexture(GL_TEXTURE_2D, t_id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 512, 512, 0, GL_RGBA, GL_UNSIGNED_BYTE, bitmapRGBA);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glBindTexture(GL_TEXTURE_2D, 0);

        texture = new Texture(t_id, 512, 512, true, false);

        MemoryUtil.memFree(bitmapRGBA);
        pc.free();

        ready = true;
    }

    /**
     * @return the cData
     */
    public STBTTPackedchar.Buffer getData() {
        return cData;
    }

    public float getHeight()
    {
        return ascent * getScale();
    }

    public float getWidth(String s)
    {
        float width = 0;

        try(MemoryStack stack = MemoryStack.stackPush())
        {
            IntBuffer advanceWidth = stack.mallocInt(1);
            IntBuffer leftSideBearing = stack.mallocInt(1);
            for(int c = 0; c < s.length(); c++)
            {
                char cp = s.charAt(c);

                stbtt_GetCodepointHMetrics(info, cp, advanceWidth, leftSideBearing);
                width += advanceWidth.get(0);
            }
        }

        return width * stbtt_ScaleForPixelHeight(info, size);
    }

    public float getScale()
    {
        return stbtt_ScaleForPixelHeight(info, size);
    }

    /**
     * @return the ascent
     */
    public int getAscent() {
        return ascent;
    }

    /**
     * @return the descent
     */
    public int getDescent() {
        return descent;
    }

    /**
     * @return the lineGap
     */
    public int getLineGap() {
        return lineGap;
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        texture.free();
        cData.free();
        data.clear();

    }

}