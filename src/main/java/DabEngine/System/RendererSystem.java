package DabEngine.System;

import DabEngine.Entities.Entity;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CBuffered;
import DabEngine.Entities.Components.CPolygon;
import DabEngine.Entities.Components.CSprite;
import DabEngine.Entities.Components.CText;
import DabEngine.Entities.Components.CTransform;
import DabEngine.Graphics.Graphics;

public class RendererSystem extends ComponentSystem {

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        g.begin(null);
        for(Entity e : EntityManager.entitiesWithComponents(CTransform.class)){
            CTransform t = e.getComponent(CTransform.class);
            if(e.hasComponent(CBuffered.class)){
                g.setRenderTarget(e.getComponent(CBuffered.class).rt);
            }
            if(e.hasComponent(CSprite.class) || e.hasComponent(CPolygon.class) || e.hasComponent(CText.class)){
                if(e.hasComponent(CSprite.class)){
                    CSprite s = e.getComponent(CSprite.class);
                    g.drawTexture(s.texture, t.pos.x, t.pos.y, t.size.x, t.size.y, t.origin.x, t.origin.y, t.rotation.z, s.color);
                }
                else if(e.hasComponent(CText.class)){
                    CText text = e.getComponent(CText.class);
                    g.drawText(text.font, text.text, t.pos.x, t.pos.y, text.color);
                }
                else if(e.hasComponent(CPolygon.class)){
                    //implement polygon rnedering
                }
            }
        }
        g.end();
    }

    
}