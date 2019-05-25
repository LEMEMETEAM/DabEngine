package DabEngine.Graphics.Models;

import Graphics.Shaders;
import org.joml.Matrix4f;

public class TextureSheet {

    private Texture texture;

    private Matrix4f scale;
    private Matrix4f translation;

    private int tilenom;

    public TextureSheet(String textureloc, int tilenom){
        this.texture = new Texture(textureloc);

        scale = new Matrix4f().scale(1.0f / (float)tilenom);
        translation = new Matrix4f();

        this.tilenom = tilenom;
    }

    public void bindTex(Shaders shaders, int x, int y){
        scale.translate(x, y, 0, translation);

        shaders.setUniform("texModifier", translation);
        texture.bind();
    }

    public void bindTex(Shaders shaders, int x, int y, int width, int height){
        scale.translate(x, y, 0, translation);
        translation.scale((float)width, (float)height, 0);

        shaders.setUniform("texModifier", translation);
        texture.bind();
    }

    public void bindTex(Shaders shaders, int tilepos){
        int x = tilepos % tilenom;
        int y = tilepos / tilenom;
        bindTex(shaders, x, y);
    }
}
