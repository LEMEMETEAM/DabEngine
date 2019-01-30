package Graphics;

import Graphics.Batch.SpriteBatch;
import org.joml.Vector4f;

import Graphics.Models.Texture;

/* Class Backround 
 * struct representing a background
 */
public class Background {
	
	public Texture texture;
	public float x, y, width, height;
	public Vector4f color = new Vector4f();
	public float z;
	
	public void draw(SpriteBatch batch) {
		batch.draw(texture, x, y, width, height, color.x, color.y, color.z, color.w, false);
	}
}