package Graphics.Models;

import Entities.GameObject;
import Entities.Components.CPhysics;
import Entities.Components.CRender;
import Entities.Components.CTransform;

public class Particle extends GameObject {
	
	public float LIFE;
	
	public Particle() {
		addComponent(new CRender());
		addComponent(new CTransform());
		addComponent(new CPhysics());
	}
}
