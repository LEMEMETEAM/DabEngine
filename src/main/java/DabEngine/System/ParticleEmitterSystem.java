package DabEngine.System;

import java.util.Random;

import org.joml.Vector3f;

import DabEngine.Resources.*;
import DabEngine.Core.App;
import DabEngine.Entities.EntityFilter;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CDynamic;
import DabEngine.Entities.Components.CParticle;
import DabEngine.Entities.Components.CParticleSpawner;
import DabEngine.Entities.Components.CSprite;
import DabEngine.Entities.Components.CTransform;
import DabEngine.Graphics.Graphics;
import DabEngine.Resources.Textures.*;
import DabEngine.Utils.Color;

public class ParticleEmitterSystem extends ComponentSystem {

    private int[] pool;
    private int size;
    private int lastUsedParticle;
    private final Random rng = new Random();
    public static boolean debug = false;
    private App app;

    public ParticleEmitterSystem(App app, int size) {
        pool = EntityManager.INSTANCE.alloc(size);
        this.app = app;
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        EntityManager em = EntityManager.INSTANCE;
        em.each(e -> {
            CParticleSpawner cs = em.component(e, CParticleSpawner.class);
            CTransform t = em.component(e, CTransform.class);

            for(int i = 0; i < 2; i++){
                respawnParticle(pool[firstUnusedParticle()], cs, t);
            }

            for(int i = 0; i < pool.length; i++){
                CParticle p = em.component(pool[i], CParticle.class);

                if(p != null){
                    CTransform p_t = em.component(pool[i], CTransform.class);
                    CDynamic d = em.component(pool[i], CDynamic.class);
                    p.lifetime -= (1/app.getConfig().targetFPS);
                    t.pos.x += d.velocity.x += (cs.force_on_particle.x / d.mass);
                    t.pos.y += d.velocity.y += (cs.force_on_particle.y / d.mass);
                    t.pos.z += d.velocity.z += (cs.force_on_particle.z / d.mass);
                    if(p.lifetime <= 0){
                        em.clear(pool[i]);
                    }
                }
            }

        }, EntityFilter.has(CParticleSpawner.class).and(CTransform.class));
    }

    private void respawnParticle(int i, CParticleSpawner spawner, CTransform spawnerPos){
        EntityManager em = EntityManager.INSTANCE;

        em.assign(i, new CParticle(){
            {
                lifetime = spawner.particleLifeTime;
            }
        }, CParticle.class);
        em.assign(i, new CTransform(){
            {
                pos.set(spawnerPos.pos).add(new Vector3f(rng.nextFloat() * spawner.spread));
                size.set(spawner.particle_size);
            }
        }, CTransform.class);
        em.assign(i, new CSprite(){
            {
                texture = ResourceManager.INSTANCE.getTexture(spawner.particle_texture, true, false);
            }
        }, CSprite.class);
        em.assign(i, new CDynamic(){
            {
                mass = spawner.particle_mass;
            }
        }, CDynamic.class);
    }

    private int firstUnusedParticle(){
        for(int i = lastUsedParticle; i < pool.length; i++){
            CParticle p = EntityManager.INSTANCE.component(pool[i], CParticle.class);
            if(p == null){
                lastUsedParticle = i;
                return i;
            }
        }

        for(int i = 0; i < lastUsedParticle; i++){
            CParticle p = EntityManager.INSTANCE.component(pool[i], CParticle.class);
            if(p == null){
                lastUsedParticle = i;
                return i;
            }
        }

        lastUsedParticle = 0;
        return 0;
        
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        // if(debug){
        //     EntityManager em = EntityManager.INSTANCE;
        //     em.each(e -> {
        //         CTransform t = em.component(e, CTransform.class);
        //         g.drawLine(t.pos.x, t.pos.y, t.pos.x, t.pos.y + 10, 0, 4, Color.RED);
        //         g.drawLine(t.pos.x, t.pos.y, t.pos.x + 10, t.pos.y, 0, 4, Color.BLUE);
        //     }, EntityFilter.has(CParticleSpawner.class).and(CTransform.class));
        // }

    }
    
}