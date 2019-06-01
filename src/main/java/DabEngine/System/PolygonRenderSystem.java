package DabEngine.System;

import DabEngine.Entities.Entity;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CEffect;
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

                //Get Polygon component
                CPolygon p = e.getComponent(CPolygon.class);

                //Get Transform component
                CTransform t = e.getComponent(CTransform.class);

                //Check if Entity has Effect component
                if(e.hasComponent(CEffect.class)){
                    //Get it
                    CEffect ef = e.getComponent(CEffect.class);

                    //Set SpriteBatch shader to effect
                    g.getBatch(PolygonBatch.class).setShader(ef.effect);
                }

                //Set Primitive Type
                g.getBatch(PolygonBatch.class).setPrimitiveType(p.primitiveType);

                //Draw
                g.getBatch(PolygonBatch.class).draw(p.polygon, t.pos.x, t.pos.y, t.size.x, t.size.y, p.color);

                //Reset shader
                g.getBatch(PolygonBatch.class).setShader(g.getBatch(PolygonBatch.class).defShader);
            }
        g.getBatch(PolygonBatch.class).end();
    }

}