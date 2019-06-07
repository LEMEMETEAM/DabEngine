package DabEngine.Graphics.Batch;

import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.BufferUtils;

import DabEngine.Utils.Utils;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryStack.*;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class Font {

    public boolean integer_align;
    private STBTTPackedchar.Buffer cData;
    private int texID;

    public Font(String fontFile, float size, int oversampling) {
        texID = glGenTextures();
        cData = STBTTPackedchar.malloc(6 * 128);

        try (STBTTPackContext pc = STBTTPackContext.malloc()) {
            ByteBuffer font = null;
			try {
				font = Utils.ioResourceToByteBuffer(fontFile, 512 * 1024);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ByteBuffer bitmap = BufferUtils.createByteBuffer(512 * 512);

			stbtt_PackBegin(pc, bitmap, 512, 512, 0, 1, NULL);
			    cData.limit(127);
                cData.position(32);
                stbtt_PackSetOversampling(pc, oversampling, 1);
                stbtt_PackFontRange(pc, font, 0, size, 32, cData);
			cData.clear();
			stbtt_PackEnd(pc);

			glBindTexture(GL_TEXTURE_2D, texID);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, 512, 512, 0, GL_RED, GL_UNSIGNED_BYTE, bitmap);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glBindTexture(GL_TEXTURE_2D, 0);
		}
    }

    public int getTexture(){
        return texID;
    }

    public STBTTPackedchar.Buffer getData(){
        return cData;
    }

}