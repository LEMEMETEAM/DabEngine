package Graphics.Models;

import Utils.ImageDecoder;

import java.nio.ByteBuffer;

import DabEngineResources.DabEngineResources;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Texture {

    private int t_id;
    private ImageDecoder decoder;
    public int width;
    public int height;
    private TextureRegion region;
    
    public Texture(Class<DabEngineResources> resources, String filename, int tileNomX, int tileNomY) {
    	decoder = new ImageDecoder(resources.getResourceAsStream(filename));

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
    
    public Texture(String filename, int tileNomX, int tileNomY){
        decoder = new ImageDecoder(filename);

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
    
    public Texture(String filename) {
    	this(filename, 1, 1);
    }
    
    public Texture(Class<DabEngineResources> resource, String filename) {
    	this(resource, filename, 1, 1);
    }
    
    public TextureRegion getRegion() {
		return region;
	}
    
    public void setRegion(int tileNomX, int tileNomY) {
    	region = new TextureRegion(tileNomX, tileNomY);
    }

    public void bind(){
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, t_id);
    }
}
