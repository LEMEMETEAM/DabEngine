package DabEngine.System;

import org.joml.Vector3f;

import DabEngine.Entities.Entity;
import DabEngine.Entities.EntityManager;
import DabEngine.Graphics.Graphics;
import DabEngine.Observer.EventManager;
import DabEngine.Utils.Pair;

import DabEngine.Entities.Components.*;

public class CollisionSystem extends ComponentSystem {

	public final float CHECK_THRESHOLD = 50f;

	@Override
	public void update() {
		// TODO Auto-generated method stub
		var entt = EntityManager.entitiesWithComponents(CCollision.class);
		for(int i = 0; i < entt.size(); i++){
			for(int j = i + 1; j < entt.size(); j++) {
				Entity e = entt.get(i);
				Entity e2 = entt.get(j);

				CCollision c = e.getComponent(CCollision.class);
				CCollision c2 = e2.getComponent(CCollision.class);
				if(c.bounds.center.distance(c2.bounds.center) > CHECK_THRESHOLD) {
					continue;
				}
				Vector3f corr;
				if((corr = c2.bounds.intersects(c.bounds)).isFinite()){
					if(corr.get(corr.maxComponent()) == corr.x){
						if(c2.bounds.center.sub(c.bounds.center, new Vector3f()).x < 0){
							e.getComponent(CTransform.class).pos.add(-corr.x, 0, 0);
						}
						else{
							e.getComponent(CTransform.class).pos.add(corr.x, 0, 0);
						}
					}
					else if(corr.get(corr.maxComponent()) == corr.y){
						if(c2.bounds.center.sub(c.bounds.center, new Vector3f()).y < 0){
							e.getComponent(CTransform.class).pos.add(0, -corr.y, 0);
						}
						else{
							e.getComponent(CTransform.class).pos.add(0, corr.y, 0);
						}
					}
					else if(corr.get(corr.maxComponent()) == corr.z){
						if(c2.bounds.center.sub(c.bounds.center, new Vector3f()).z < 0){
							e.getComponent(CTransform.class).pos.add(0, 0, -corr.z);
						}
						else{
							e.getComponent(CTransform.class).pos.add(0, 0, corr.z);
						}
					}
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
}