package DabEngine.System;

import DabEngine.Entities.Entity;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CAnimation;
import DabEngine.Graphics.Graphics;

public class AnimationSystem extends ComponentSystem {

    @Override
    public void update() {
        for(Entity e : EntityManager.entitiesWithComponents(CAnimation.class)){
            CAnimation a = e.getComponent(CAnimation.class);
            a.currentFrame++;
            if(a.currentFrame >= a.delays.get(a.texSheetFrame)) {
                a.texSheetFrame++; 
                if(a.texSheetFrame > a.frames.size() - 1) {
                    a.texSheetFrame = a.looped ? 0 : a.frames.size();
                }
                a.currentFrame = 0;
            }
        }
    }

    @Override
    public void render(Graphics g) {

    }
    
}