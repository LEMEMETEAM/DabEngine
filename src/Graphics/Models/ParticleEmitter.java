package Graphics.Models;

import java.lang.ref.WeakReference;

import Entities.GameObject;
import Entities.Components.CRender;
import Entities.Components.CTransform;
import System.RenderSystem;
import System.System;

public class ParticleEmitter extends System {
	
	private int max_particles = 500;
	
	public ParticleEmitter() {
		for(int i = 0; i < max_particles; i++) {
			addGameObject(new Particle());
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		int new_particles = 2;
		for(int i = 0; i < new_particle; i++) {
			int unusedParticle = 
		}
		for(int i = 0; i < max_particles; i++) {
			Particle p = (Particle) obj.get(i).get();
			p.LIFE -= 0.1f;
			if(p.LIFE > 0) {
				CRender render = p.getComponent(CRender.class);
				render.getColor().w -= 0.1f * 2.5f;
			}
		}
	}
	
	public int firstUnusedParticle() {
		int lastUsedParticle = 0;
		for(int i = lastUsedParticle; i < max_particles; i++) {
			Particle p = (Particle) obj.get(i).get();
			if(p.LIFE <= 0) {
				lastUsedParticle = i;
				return i;
			}
		}
		for(int i = 0; i < lastUsedParticle; i++) {
			Particle p = (Particle) obj.get(i).get();
			if(p.LIFE <= 0) {
				lastUsedParticle = i;
				return i;
			}
		}
		lastUsedParticle = 0;
		return i;
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		for(WeakReference<GameObject> particle : obj) {
			Particle p = (Particle) particle.get();
			RenderSystem render = this.state.get().getSystem(RenderSystem.class);
			p.getComponent(CRender.class).render(render.getBatch());
		}
	}
}
