package DabEngine.Graphics.OpenGL.Textures;

import static org.lwjgl.opengl.GL33.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import DabEngine.Utils.ImageDecoder;
import DabEngine.Utils.Utils;

/**
 * Essentially just a handle for the texture id, you can create one from a byte buffer
 * and a you can create a empty/blnk texture for framebuffers and other tings.
 */
public class Texture {

    private int t_id;
    public enum Parameters{
        REPEAT,
        MIRRORED,
        CLAMP_TO_EDGE,
        CLAMP_TO_BORDER,
        NEAREST,
        LINEAR,
        NEAREST_LINEAR;
    };
    private int width, height;

    public Texture(ByteBuffer data, int width, int height, Texture.Parameters... params) {
        t_id = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, t_id);

        for(Texture.Parameters p : params){
            switch(p){
                case REPEAT:
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
                    break;
                
                case MIRRORED:
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

                case NEAREST:
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
                    break;

                case LINEAR:
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                    break;

                default:
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            }
        }

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        

        this.width = width;
        this.height = height;

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public Texture(int width, int height, boolean white, Texture.Parameters... params){
        ByteBuffer buf = BufferUtils.createByteBuffer(width * height * 4);
        for(int i = 0; i < buf.limit(); i++){
            if(white)
                buf.put(i, (byte)(255 & 0xFF));
            else
                buf.put(i, (byte)(0 & 0xFF));
        }
        t_id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, t_id);

        for(Texture.Parameters p : params){
            switch(p){
                case REPEAT:
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
                    break;
                
                case MIRRORED:
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

                case NEAREST:
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
                    break;

                case LINEAR:
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                    break;

                default:
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            }
        }
        
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    /**
     * Binds this Texture Handle to an already made OpenGL Texture.
     * @param id the opeGL texture id to use
     */
    public Texture(int id){
        t_id = id;
    }

    public void bind(int sample){
        glActiveTexture(GL_TEXTURE0 + sample);
        glBindTexture(GL_TEXTURE_2D, t_id);
    }

    public int getID(){
        return t_id;
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
}
