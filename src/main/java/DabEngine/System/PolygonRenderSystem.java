package DabEngine.System;

import DabEngine.Entities.Entity;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CPolygon;
import DabEngine.Entities.Components.CTransform;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.Batch.PolygonBatch;

public class PolygonRenderSystem extends ComponentSystem {

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        g.getBatch(PolygonBatch.class).begin();
            for(Entity e : EntityManager.entitiesWithComponents(CTransform.class, CPolygon.class)){
                CPolygon p = e.getComponent(CPolygon.class);
                CTransform t = e.getComponent(CTransform.class);
                g.getBatch(PolygonBatch.class).setPrimitiveType(p.primitiveType);
                g.getBatch(PolygonBatch.class).draw(p.polygon, t.pos.x, t.pos.y, t.size.x, t.size.y, p.color);
            }
        g.getBatch(PolygonBatch.class).end();
    }

}