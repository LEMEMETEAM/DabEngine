package DabEngine.Graphics.Models;

import DabEngine.Entities.Entity;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CPhysics;
import DabEngine.Entities.Components.CSprite;
import DabEngine.Entities.Components.CTransform;
import DabEngine.Entities.Components.Component;

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
