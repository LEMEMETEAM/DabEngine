package System;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.logging.Level;

import Entities.Entity;
import Entities.EntityManager;
import Entities.Components.CSprite;
import Entities.Components.CTransform;
import Graphics.Batch.SpriteBatch;

public class RenderSystem extends System {
	
	private SpriteBatch batch;
	
	@Override
	public void update(double delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		batch.begin();
		for(Entity e : EntityManager.entitiesWithComponents(CSprite.class, CTransform.class)) {
			CSprite render = e.getComponent(CSprite.class);
			CTransform trans = e.getComponent(CTransform.class);	
			batch.draw(render.texture, trans.pos.x, trans.pos.y, trans.size.x, trans.size.y, render.color.x, render.color.y, render.color.z, render.color.w, render.center_anchor);
		}
		batch.end();
	}
	
	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}
	
	public SpriteBatch getBatch() {
		return batch;
	}
}
