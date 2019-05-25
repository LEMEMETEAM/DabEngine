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
import DabEngine.Graphics.Batch.PolygonBatch;
import DabEngine.Graphics.Models.ParticleFactory.CParticle;
import DabEngine.System.ComponentSystem;

public class ParticleEmitter extends ComponentSystem {
	
	private int max_particles = 50;
	public Vector3f pos;
	public static float OFFSET = 5;
	private Entity[] particles = new Entity[max_particles];
	
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
				render.color.w -= 0.1f * 2.5f;
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
		Random rng = new Random();
		CTransform trans = p.getComponent(CTransform.class);
		CSprite render = p.getComponent(CSprite.class);
		CParticle particle = p.getComponent(CParticle.class);
		
		float random = ((rng.nextFloat() * 100) - 50) / 10f;
		float rColor = 0.5f + rng.nextFloat();
		
		trans.pos.set(this.pos.add(random, random, 0, new Vector3f()).add(offset, offset, 0, new Vector3f()));
		render.color = new Vector4f(rColor, rColor, rColor, 1.0f);
		particle.LIFE = 1.0f; 
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		PolygonBatch pb = g.getBatch(PolygonBatch.class);
		pb.begin(GL11.GL_TRIANGLES);
		for(Entity particle : particles) {
			CParticle p = particle.getComponent(CParticle.class);
			if(p.LIFE > 0.0f) {
				CSprite render = particle.getComponent(CSprite.class);
				CTransform transform = particle.getComponent(CTransform.class);
				Polygon poly = new Polygon(
					new int[] {
						0,1,2,
						0,3,2
					},
					new Vector2f[] {
						new Vector2f(-1, -1),
						new Vector2f(-1, 1),
						new Vector2f(1, 1),
						new Vector2f(1, -1)
					}
				);
				pb.draw(poly, transform.pos.x, transform.pos.y, transform.size.x, transform.size.y, render.color);
			}
		}
		pb.end();
	}
}
