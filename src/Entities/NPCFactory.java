package Entities;

import org.joml.Vector3f;
import org.joml.Vector4f;

import Entities.Components.CInteract;
import Entities.Components.CNPC;
import Entities.Components.CSprite;
import Entities.Components.CTransform;
import Graphics.Models.Texture;

public class NPCFactory {
	
	@SuppressWarnings("serial")
	public static Entity spawnNPC(String n, Texture tex, float x, float y, float z, float width, float height, float depth, Vector4f c, boolean bcenter_anchor) {
		return EntityManager.createEntity(
			new CSprite() {
				{
					texture = tex;
					color = c;
					center_anchor = bcenter_anchor;
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
