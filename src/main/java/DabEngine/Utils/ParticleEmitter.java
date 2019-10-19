import java.util.Arrays;
import java.util.Random;

import org.joml.Vector3f;

import DabEngine.Entities.Components.CParticleSpawner;
import DabEngine.Graphics.OpenGL.Textures.Texture;

//TODO: change package

public class ParticleEmitter {

    private class Particle {
        Vector3f pos;
        float w, h, l;
        float lifetime;
    }

    private Particle[] particles;
    private Texture particle_texture;
    private int particle_number;

    private Random rng = new Random();

    protected ParticleEmitter(int part_num){
        particle_number = part_num;
        particles = new Particle[particle_number];
        Arrays.fill(particles, new Particle());
    }

    public void setParticleTexture(Texture texture){
        this.particle_texture = texture;
    }

    public void update(){
        for(int i = 0; i < 2; i++){

        }
    }

    private void respawnParticle(Particle p, Vector3f pos, float spreadx, float spreadz, Vector3f axis, float rot){
        p.pos.x = pos.x + (rng.nextFloat() * spreadx);
        p.pos.y = pos.y;
        p.pos.z = pos.z + (rng.nextFloat() * spreadz);
        p.pos.rotateAxis(rot, axis.x, axis.y, axis.z);
        
    }

    public void importParticleSpawnerEntity(CParticleSpawner sp){

    }
}