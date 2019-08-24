package DabEngine.Graphics.Batch;

import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.BufferUtils;

import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Utils.Pair;
import DabEngine.Utils.Utils;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryStack.*;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * class that creates a texture atlas from a given TrueType file.
 */
public class Font {

    public boolean integer_align;
    private STBTTPackedchar.Buffer cData;
    private Texture texture;
    public float size;
    public static final Shaders TEXT_DEFAULT_SHADER = Shaders.getUberShader("/Shaders/default.vs", "/Shaders/default.fs", new Pair<>("TEXTURED", "0"), new Pair<>("TEXT", "0"));

    /**
     * creates the texture atlas for the specified ttf file.
     * @param fontFile the file path of the font
     * @param size the size of the glyphs
     * @param oversampling oversampling value
     */
    public Font(String fontFile, float size, int oversampling) {
        int texID = glGenTextures();
        cData = STBTTPackedchar.malloc(6 * 128);

        try (STBTTPackContext pc = STBTTPackContext.malloc()) {
            ByteBuffer font = null;
			try {
				font = Utils.ioResourceToByteBuffer(fontFile, 512 * 1024);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            ByteBuffer bitmap;
            try(MemoryStack stack = stackPush()){
                bitmap = stack.calloc(512 * 512);
            }

			stbtt_PackBegin(pc, bitmap, 512, 512, 0, 1, NULL);
			    cData.limit(127);
                cData.position(32);
                stbtt_PackSetOversampling(pc, oversampling, 1);
                stbtt_PackFontRange(pc, font, 0, size, 32, cData);
			cData.clear();
			stbtt_PackEnd(pc);

			glBindTexture(GL_TEXTURE_2D, texID);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_R16, 512, 512, 0, GL_RED, GL_UNSIGNED_BYTE, bitmap);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glBindTexture(GL_TEXTURE_2D, 0);

            texture = new Texture(texID);

            this.size = size;
		}
    }

    /**
     * Gets the texture atlas for this font
     * @return the texture
     */
    public Texture getTexture(){
        return texture;
    }

    /**
     * Get the character data.
     * @return the data
     */
    public STBTTPackedchar.Buffer getData(){
        return cData;
    }

}