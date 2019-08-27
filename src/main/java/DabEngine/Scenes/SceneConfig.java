package DabEngine.Scenes;

import DabEngine.Graphics.OpenGL.Textures.Texture;

public class SceneConfig {
    public boolean renderToTexture;
    public int renderTextureAmmount;
    public boolean srgb;
    public boolean hdr;

    public void renderToTexture(boolean b, int ammount){
        this.renderToTexture = b;
        if(b){
            renderTextureAmmount = ammount;
        }
    }

    public void srgb(boolean b){
        this.srgb = b;
    }

    public void hdr(boolean b){
        this.hdr = b;
    }
}