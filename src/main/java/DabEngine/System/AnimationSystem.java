package DabEngine.System;

import DabEngine.Entities.EntityFilter;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CAnimation;
import DabEngine.Graphics.Graphics;

public class AnimationSystem extends ComponentSystem {

    @Override
    public void update() {
        EntityManager.INSTANCE.each((e) -> {
            CAnimation a = EntityManager.INSTANCE.component(e, CAnimation.class);
            a.currentFrame++;
            if(a.currentFrame >= a.delays.get(a.texSheetFrame)) {
                a.texSheetFrame++; 
                if(a.texSheetFrame > a.frames.size() - 1) {
                    a.texSheetFrame = a.looped ? 0 : a.frames.size();
                }
                a.currentFrame = 0;
            }
        }, EntityFilter.has(CAnimation.class));
    }

    @Override
    public void render(Graphics g) {

    }
    
}