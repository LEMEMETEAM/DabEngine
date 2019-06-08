package DabEngine.Graphics.Models;

import org.joml.Vector3f;
import org.joml.Vector4f;

import DabEngine.Entities.Entity;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CPhysics;
import DabEngine.Entities.Components.CPhysics.BodyType;
import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Utils.Color;
import DabEngine.Entities.Components.CSprite;
import DabEngine.Entities.Components.CTile;
import DabEngine.Entities.Components.CTransform;

public class TileFactory extends Entity {
	
	public static Entity spawnTile(Texture tex, float x, float y, float z, float width, float height, float depth, Color c, boolean b_destroyable, BodyType type) {
		return EntityManager.createEntity(
			new CSprite() {
				{
					texture = tex;
					color = c;
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