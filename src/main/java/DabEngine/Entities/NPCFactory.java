package DabEngine.Entities;

import org.joml.Vector3f;
import org.joml.Vector4f;

import DabEngine.Entities.Components.CInteract;
import DabEngine.Entities.Components.CNPC;
import DabEngine.Entities.Components.CSprite;
import DabEngine.Entities.Components.CTransform;
import DabEngine.Graphics.Models.Texture;

public class NPCFactory {
	
	@SuppressWarnings("serial")
	public static Entity spawnNPC(String n, Texture tex, float x, float y, float z, float width, float height, float depth, Vector4f c) {
		return EntityManager.createEntity(
			new CSprite() {
				{
					texture = tex;
					color = c;
				}
			},
			new CTransform() {
				{
					pos = new Vector3f(x, y, z);
					size = new Vector3f(width, height, depth);
				}
			},
			new CNPC() {
				{
					name = n;
				}
			},
			new CInteract()
		);
	}
}
