package DabEngine.System;

import DabEngine.Entities.Entity;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CEffect;
import DabEngine.Entities.Components.CSprite;
import DabEngine.Entities.Components.CTransform;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.Batch.SpriteBatch;

/**
 * Default Sprite Render System.
 * Uses a {@link DabEngine.Graphics.Batch.SpriteBatch}
 */
public class SpriteRenderSystem extends ComponentSystem {

    @Override
    public void update() {
        
    }

    @Override
    public void render(Graphics g) {
        g.getBatch(SpriteBatch.class).begin();
        for(Entity e : EntityManager.entitiesWithComponents(CTransform.class, CSprite.class)){
            //Get Sprite Component
            CSprite sprite = e.getComponent(CSprite.class);

            //Get Transform Component
            CTransform transform = e.getComponent(CTransform.class);

            //Check If Entity has Effect Component
            if(e.hasComponent(CEffect.class)){
                //Get It
                CEffect ef = e.getComponent(CEffect.class);
                
                //Then set that shader to SpriteBatch
                g.getBatch(SpriteBatch.class).setShader(ef.effect);
                
            }

            //Draw
            g.getBatch(SpriteBatch.class).draw(sprite.texture, transform.pos.x, transform.pos.y, transform.size.x, transform.size.y, transform.rotation.z, transform.origin.x, transform.origin.y, sprite.color.x, sprite.color.y, sprite.color.z, sprite.color.w);

            //Set Shader Back To Default
            g.getBatch(SpriteBatch.class).setShader(g.getBatch(SpriteBatch.class).defShader);

        }
        g.getBatch(SpriteBatch.class).end();
    }

}