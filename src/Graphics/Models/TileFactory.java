package Graphics.Models;

import org.joml.Vector3f;
import org.joml.Vector4f;

import Entities.Entity;
import Entities.EntityManager;
import Entities.Components.CPhysics;
import Entities.Components.CPhysics.BodyType;
import Entities.Components.CSprite;
import Entities.Components.CTile;
import Entities.Components.CTransform;

public class TileFactory extends Entity {
	
	@SuppressWarnings("serial")
	public static Entity spawnTile(Texture tex, float x, float y, float z, float width, float height, float depth, Vector4f c, boolean bcenter_anchor, boolean b_destroyable, BodyType type) {
		return EntityManager.createEntity(
			new CSprite() {
				{
					texture = tex;
					color = c;
					center_anchor = bcenter_anchor;
				}
			},
			new CPhysics() {
				{
					bodytype = type;
				}
			},
			new CTransform() {
				{
					pos = new Vector3f(x, y, z);
					size = new Vector3f(width, height, depth);
				}
			},
			new CTile() {
				{
					destroyable = b_destroyable;
				}
			}
		);
	}
}