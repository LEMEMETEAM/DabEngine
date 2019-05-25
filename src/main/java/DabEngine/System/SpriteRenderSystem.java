package DabEngine.System;

import DabEngine.Entities.Entity;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CSprite;
import DabEngine.Entities.Components.CTransform;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.Batch.SpriteBatch;

public class SpriteRenderSystem extends ComponentSystem {

    @Override
    public void update() {
        
    }

    @Override
    public void render(Graphics g) {
        g.getBatch(SpriteBatch.class).begin();
        for(Entity e : EntityManager.entitiesWithComponents(CTransform.class, CSprite.class)){
            CSprite sprite = e.getComponent(CSprite.class);
            CTransform transform = e.getComponent(CTransform.class);
            g.getBatch(SpriteBatch.class).draw(sprite.texture, transform.pos.x, transform.pos.y, transform.size.x, transform.size.y, transform.rotation.z, transform.origin.x, transform.origin.y, sprite.color.x, sprite.color.y, sprite.color.z, sprite.color.w);
        }
        g.getBatch(SpriteBatch.class).end();
    }

}