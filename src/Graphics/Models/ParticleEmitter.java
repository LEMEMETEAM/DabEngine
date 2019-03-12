package Graphics.Models;

import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.logging.Level;

import org.joml.Vector2f;
import org.joml.Vector4f;

import Entities.Entity;
import Entities.Components.CPhysics;
import Entities.Components.CSprite;
import Entities.Components.CTransform;
import System.RenderSystem;
import System.System;

public class ParticleEmitter extends System {
	
	private int max_particles = 2;
	public Vector2f pos;
	public static float OFFSET = 5;
	public static Texture GLOBAL_PARTICLE_TEXTURE;
	
	public ParticleEmitter() {
		pos = new Vector2f();
		for(int i = 0; i < max_particles; i++) {
			addGameObject
			(
				new Particle() {{
					CSprite render = getComponent(CSprite.class);
					render.center_anchor = true;
					render.texture = GLOBAL_PARTICLE_TEXTURE;
				}}
			);
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		int new_particles = 2;
		for(int i = 0; i < new_particles; i++) {
			int unusedParticle = firstUnusedParticle();
			respawnParticle((Particle) obj.get(unusedParticle).get(), OFFSET);
		}
		for(int i = 0; i < max_particles; i++) {
			Particle p = (Particle) obj.get(i).get();
			p.LIFE -= 0.01f;
			if(p.LIFE > 0) {
				CTransform transform = p.getComponent(CTransform.class);
				CSprite render = p.getComponent(CSprite.class);
				CPhysics physics = p.getComponent(CPhysics.class);
				render.color.w -= 0.1f * 2.5f;
				transform.pos = transform.pos.sub(physics.velocity);
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
		return 0;
	}
	
	public void respawnParticle(Particle p, float offset) {
		Random rng = new Random();
		CTransform trans = p.getComponent(CTransform.class);
		CSprite render = p.getComponent(CSprite.class);
		CPhysics physics = p.getComponent(CPhysics.class);
		
		float random = ((rng.nextFloat() * 100) - 50) / 10f;
		float rColor = 0.5f + rng.nextFloat();
		
		trans.pos.set(this.pos.add(random, random, new Vector2f()).add(offset, offset, new  Vector2f()));
		render.color = new Vector4f(rColor, rColor, rColor, 1.0f);
		p.LIFE = 1.0f; 
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		for(WeakReference<Entity> particle : obj) {
			Particle p = (Particle) particle.get();
			if(p.LIFE > 0.0f) {
				RenderSystem renderer = this.state.get().getSystem(RenderSystem.class);
				CSprite render = p.getComponent(CSprite.class);
				CTransform transform = p.getComponent(CTransform.class);
				renderer.getBatch().begin();
				renderer.getBatch().draw(render.texture, transform.pos.x, transform.pos.y, transform.size.x, transform.size.y, render.color.x, render.color.y, render.color.z, render.color.w, render.center_anchor);
				renderer.getBatch().end();
			}
		}
	}
}
