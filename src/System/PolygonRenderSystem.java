package System;

import Entities.Entity;
import Entities.EntityManager;
import Entities.Components.CPolygon;
import Entities.Components.CTransform;
import Graphics.Graphics;
import Graphics.Batch.PolygonBatch;

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