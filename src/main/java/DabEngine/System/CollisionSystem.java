package DabEngine.System;

import java.util.ArrayList;

import org.joml.Vector3f;

import DabEngine.Entities.EntityFilter;
import DabEngine.Entities.EntityManager;
import DabEngine.Graphics.Graphics;
import DabEngine.Observer.EventManager;
import DabEngine.Utils.Pair;

import DabEngine.Entities.Components.*;

public class CollisionSystem extends ComponentSystem {

	public final float CHECK_THRESHOLD = 50f;
	public ArrayList<Integer> ids = new ArrayList<>();
	public static boolean draw_bounds;

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
		EntityManager.INSTANCE.each((e) -> {
			CTransform t = EntityManager.INSTANCE.component(e, CTransform.class);
			CCollision c = EntityManager.INSTANCE.component(e, CCollision.class);
			if(EntityManager.INSTANCE.has(e, CDynamic.class)){
				c.bounds.center = t.pos.add(t.size.div(2, new Vector3f()), new Vector3f());
				c.bounds.half_extent = t.size.div(2, new Vector3f());
			}
			ids.add(e);
		}, EntityFilter.has(CCollision.class).and(CTransform.class));

		for(int i = 0; i < ids.size(); i++){
			for(int j = i + 1; j < ids.size(); j++) {
				int e = ids.get(i);
				int e2 = ids.get(j);

				CCollision c = EntityManager.INSTANCE.component(e, CCollision.class);
				CCollision c2 = EntityManager.INSTANCE.component(e2, CCollision.class);
				Vector3f corr;
				if((corr = c2.bounds.intersects(c.bounds)).isFinite()){
					EventManager.INSTANCE.submit(new CollisionEvent(new Pair<>(e, e2), corr, c2.bounds.center.sub(c.bounds.center, new Vector3f())), CollisionEvent.class);
				}
			}
		}
		if(!draw_bounds)
			ids.clear();
		
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub

	}
	
}