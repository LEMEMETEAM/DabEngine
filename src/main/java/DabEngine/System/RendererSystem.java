package DabEngine.System;

import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CBuffered;
import DabEngine.Entities.Components.CPolygon;
import DabEngine.Entities.Components.CSprite;
import DabEngine.Entities.Components.CText;
import DabEngine.Entities.Components.CTransform;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.OpenGL.RenderTarget;
import DabEngine.Graphics.OpenGL.Viewport.Viewport;

public class RendererSystem extends ComponentSystem {

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        EntityManager.INSTANCE.each(e -> {
            CTransform t = EntityManager.INSTANCE.component(e, CTransform.class);
            if(EntityManager.INSTANCE.has(e, CBuffered.class)){
                g.setRenderTarget(EntityManager.INSTANCE.component(e, CBuffered.class).rt);
            }
            if(EntityManager.INSTANCE.has(e, CSprite.class) || EntityManager.INSTANCE.has(e, CPolygon.class) || EntityManager.INSTANCE.has(e, CText.class)){
                if(EntityManager.INSTANCE.has(e, CSprite.class)){
                    CSprite s = EntityManager.INSTANCE.component(e, CSprite.class);
                    g.drawTexture(s.texture, s.region, t.pos.x, t.pos.y, t.pos.z, t.size.x, t.size.y, t.origin.x, t.origin.y, t.rotation.z, s.color);
                }
                else if(EntityManager.INSTANCE.has(e, CText.class)){
                    CText text = EntityManager.INSTANCE.component(e, CText.class);
                    g.pushShader(text.textShader);
                    g.drawText(text.font, text.text, t.pos.x, t.pos.y, t.pos.z, text.color);
                    g.popShader();
                }
                else if(EntityManager.INSTANCE.has(e, CPolygon.class)){
                    //implement polygon rnedering
                }
            }
        }, CTransform.class);
    }

    
}