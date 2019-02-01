package System;

import java.lang.ref.WeakReference;

import Entities.GameObject;
import Entities.Components.CRender;
import Entities.Components.CTransform;
import Graphics.Batch.SpriteBatch;

public class RenderSystem extends System {
	
	private SpriteBatch batch;
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		batch.begin();
		for(WeakReference<GameObject> object : obj) {
			CRender render = object.get().getComponent(CRender.class);
			CTransform trans = object.get().getComponent(CTransform.class);	
			batch.draw(render.texture, trans.pos.x, trans.pos.y, trans.size.x, trans.pos.y, render.color.x, render.color.y, render.color.z, render.color.w, render.center_anchor);
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
