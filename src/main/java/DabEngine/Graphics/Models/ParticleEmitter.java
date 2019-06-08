package DabEngine.Graphics.Models;

import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import DabEngine.Entities.Entity;
import DabEngine.Entities.Components.CPhysics;
import DabEngine.Entities.Components.CSprite;
import DabEngine.Entities.Components.CTransform;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.Batch.Polygon;
import DabEngine.Graphics.Models.ParticleFactory.CParticle;
import DabEngine.System.ComponentSystem;
import DabEngine.Utils.Color;

public class ParticleEmitter extends ComponentSystem {
	
	private int max_particles = 50;
	public Vector3f pos;
	public static float OFFSET = 5;
	private Entity[] particles = new Entity[max_particles];
	private  Random rng = new Random();
	
	public ParticleEmitter(float particleLife) {
		pos = new Vector3f();
		for(int i = 0; i < max_particles; i++) {
			particles[i] = ParticleFactory.spawnParticle(particleLife);
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		int new_particles = 2;
		for(int i = 0; i < new_particles; i++) {
			int unusedParticle = firstUnusedParticle();
			respawnParticle(particles[unusedParticle], OFFSET);
		}
		for(int i = 0; i < max_particles; i++) {
			Entity p = particles[i];
			float life = p.getComponent(ParticleFactory.CParticle.class).LIFE;
			life -= 0.01f;
			if(life > 0) {
				CTransform transform = p.getComponent(CTransform.class);
				CSprite render = p.getComponent(CSprite.class);
				CPhysics physics = p.getComponent(CPhysics.class);
				render.color.TL[3] -= 0.1f * 2.5f;
				render.color.BL[3] -= 0.1f * 2.5f;
				render.color.BR[3] -= 0.1f * 2.5f;
				render.color.TR[3] -= 0.1f * 2.5f;
				transform.pos = transform.pos.sub(physics.velocity);
			}
		}
	}
	
	public int firstUnusedParticle() {
		int lastUsedParticle = 0;
		for(int i = lastUsedParticle; i < max_particles; i++) {
			CParticle p = particles[i].getComponent(CParticle.class);
			if(p.LIFE <= 0) {
				lastUsedParticle = i;
				return i;
			}
		}
		for(int i = 0; i < lastUsedParticle; i++) {
			CParticle p = particles[i].getComponent(CParticle.class);
			if(p.LIFE <= 0) {
				lastUsedParticle = i;
				return i;
			}
		}
		lastUsedParticle = 0;
		return 0;
	}
	
	public void respawnParticle(Entity p, float offset) {
		CTransform trans = p.getComponent(CTransform.class);
		CSprite render = p.getComponent(CSprite.class);
		CParticle particle = p.getComponent(CParticle.class);
		
		float random = ((rng.nextFloat() * 100) - 50) / 10f;
		float rColor = 0.5f + rng.nextFloat();
		
		trans.pos.set(this.pos.add(random, random, 0, new Vector3f()).add(offset, offset, 0, new Vector3f()));
		float[] col = new float[]{
			rColor, rColor, rColor, 1.0f
		};
		render.color = new Color(col, col, col, col);
		particle.LIFE = 1.0f; 
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		g.begin();
		for(Entity particle : particles) {
			CParticle p = particle.getComponent(CParticle.class);
			if(p.LIFE > 0.0f) {
				CSprite render = particle.getComponent(CSprite.class);
				CTransform transform = particle.getComponent(CTransform.class);
				g.fillRect(transform.pos.x, transform.pos.y, transform.size.x, transform.size.y, transform.origin.x, transform.origin.y, transform.rotation.z, render.color);
			}
		}
		g.end();
	}
}
