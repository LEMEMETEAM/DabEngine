package DabEngine.System;

import DabEngine.Entities.EntityFilter;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CParticleSpawner;
import DabEngine.Graphics.Graphics;

public class ParticleEmitterSystem extends ComponentSystem {

    private ParticleEmitter pm = new ParticleEmitter()

    @Override
    public void update() {
        // TODO Auto-generated method stub
        EntityManager.INSTANCE.each(e -> {

        }, EntityFilter.has(CParticleSpawner.class).and(CTransform.class));
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub

    }
    
}