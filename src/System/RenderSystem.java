package System;

import java.lang.ref.WeakReference;

import Entities.GameObject;
import Entities.Components.CRender;
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
			object.get().getComponent(CRender.class).render(batch);
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
