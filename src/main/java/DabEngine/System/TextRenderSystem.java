package DabEngine.System;

import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CText;
import DabEngine.Entities.Components.CTransform;
import DabEngine.Graphics.Batch.TextBatch;

public class TextRenderSystem extends ComponentSystem {
    
    @Override
    public void update() {
        
    }

    @Override
    public void render(Graphics g) {
        g.getBatch(TextBatch.class).begin();
        for(Entity e : EntityManager.entitiesWithComponents(CTransform.class, CText.class)){
            CText text = e.getComponent(CText.class);
            CTransform transform = e.getComponent(CTransform.class);
            g.getBatch(TextBatch.class).draw(text.text, transform.pos.x, transform.pos.y, transform.size.y, text.color);
        }
        g.getBatch(TextBatch.class).end();
    }

}