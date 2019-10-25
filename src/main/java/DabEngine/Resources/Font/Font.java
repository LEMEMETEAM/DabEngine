package DabEngine.Resources.Font;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RED;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL30.GL_R16;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBTruetype.stbtt_PackBegin;
import static org.lwjgl.stb.STBTruetype.stbtt_PackEnd;
import static org.lwjgl.stb.STBTruetype.stbtt_PackFontRange;
import static org.lwjgl.stb.STBTruetype.stbtt_PackSetOversampling;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.MemoryUtil;

import DabEngine.Resources.Resource;
import DabEngine.Resources.Textures.Texture;
import DabEngine.Utils.Utils;

/**
 * class that creates a texture atlas from a given TrueType file.
 */
public class Font extends Resource 
{

    public boolean integer_align;
    private STBTTPackedchar.Buffer cData;
    private Texture texture;
    public float size;
    private int oversampling;

    /**
     * creates the texture atlas for the specified ttf file.
     * @param fontFile the file path of the font
     * @param size the size of the glyphs
     * @param oversampling oversampling value
     */
    public Font(String fontFile, float size, int oversampling) {
        super(fontFile);

        integer_align = false;
        this.size = size;
        this.oversampling = oversampling;

        texture = null;
        cData = null;
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

        ready = loadFont();
    }

    private boolean loadFont()
    {
        int texID = glGenTextures();
        cData = STBTTPackedchar.malloc(6 * 128);

        try (STBTTPackContext pc = STBTTPackContext.malloc()) {
            ByteBuffer font = null;
			try {
				font = Utils.ioResourceToByteBuffer(filename, 512 * 1024);
			} catch (IOException e) {
				// TODO Auto-generated catch block
                e.printStackTrace();
                return true;
			}

            ByteBuffer bitmap = MemoryUtil.memCalloc(512 * 512);

			stbtt_PackBegin(pc, bitmap, 512, 512, 0, 1, NULL);
			    cData.limit(127);
                cData.position(32);
                stbtt_PackSetOversampling(pc, oversampling, 1);
                stbtt_PackFontRange(pc, font, 0, size, 32, cData);
			cData.clear();
			stbtt_PackEnd(pc);

			glBindTexture(GL_TEXTURE_2D, texID);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_R16, 512, 512, 0, GL_RED, GL_UNSIGNED_BYTE, bitmap);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glGenerateMipmap(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, 0);

            texture = new Texture(texID, 512, 512, true, false);

        }
        
        return false;
    }

    /**
     * @return the cData
     */
    public STBTTPackedchar.Buffer getData() {
        return cData;
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        texture.free();

        integer_align = false;

    }

}