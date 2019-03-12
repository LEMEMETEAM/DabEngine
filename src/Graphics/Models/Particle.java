package Graphics.Models;

import Entities.Entity;
import Entities.Components.CPhysics;
import Entities.Components.CSprite;
import Entities.Components.CTransform;

public class Particle extends Entity {
	
	public float LIFE;
	
	public Particle() {
		addComponent(new CSprite());
		addComponent(new CTransform());
		addComponent(new CPhysics());
	}
}
