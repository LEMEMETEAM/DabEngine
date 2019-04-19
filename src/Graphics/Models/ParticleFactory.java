package Graphics.Models;

import Entities.Entity;
import Entities.EntityManager;
import Entities.Components.CPhysics;
import Entities.Components.CSprite;
import Entities.Components.CTransform;
import Entities.Components.Component;

public class ParticleFactory {
	
	public float LIFE;
	
	public static class CParticle extends Component {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2524299250221959272L;
		public float LIFE;
	}
	
	@SuppressWarnings("serial")
	public static Entity spawnParticle(float life) {
		return EntityManager.createEntity(new CSprite(), new CTransform(), new CPhysics(), new CParticle() {
			{
				LIFE = life;
			}
		});
	}
}
