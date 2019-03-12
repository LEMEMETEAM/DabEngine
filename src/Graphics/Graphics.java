package Graphics;

import org.joml.Vector2f;
import org.joml.Vector4f;

import Core.Engine;
import DabEngineResources.DabEngineResources;
import Graphics.Batch.IBatch;
import Graphics.Batch.Polygon;
import Graphics.Batch.PolygonBatch;
import Graphics.Batch.SpriteBatch;
import Graphics.Batch.TextBatch;
import Graphics.Models.Texture;

public class Graphics {
	
	private SpriteBatch sprite;
	private TextBatch text;
	private PolygonBatch polygon;
	private Window window;
	
	public Graphics(Engine e) {
		window = e.getMainWindow();
		
		sprite = new SpriteBatch();
		text = new TextBatch(new Texture(DabEngineResources.class, "Fonts/Consolas_font.png", 16, 16));
		polygon = new PolygonBatch();
	}
	
	public <T> T getBatch(Class<T> cls) {
		if(cls.isInstance(sprite)) {
			return cls.cast(sprite);
		}
		else if(cls.isInstance(text)) {
			return cls.cast(text);
		}
		else if(cls.isInstance(polygon)){
			return cls.cast(polygon);
		}
		return null;
	}
}
