package DabEngine.System;

import DabEngine.Entities.Entity;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CText;
import DabEngine.Entities.Components.CTransform;
import DabEngine.Graphics.Graphics;
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
            //change  later to add oversampling to component
            g.getBatch(TextBatch.class).draw(text.text, transform.pos.x, transform.pos.y, transform.size.y, 0, text.color.x, text.color.y, text.color.z, text.color.w);
        }
        g.getBatch(TextBatch.class).end();
    }

}