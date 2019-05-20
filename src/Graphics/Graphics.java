package Graphics;


import Cache.ResourceManager;
import Core.Engine;
import DabEngineResources.DabEngineResources;
import Graphics.Batch.ModelBatch;
import Graphics.Batch.PolygonBatch;
import Graphics.Batch.SpriteBatch;
import Graphics.Batch.TextBatch;
import Graphics.Models.Texture;

public class Graphics {
	
	private SpriteBatch sprite;
	private TextBatch text;
	private PolygonBatch polygon;
	private ModelBatch model;
	@SuppressWarnings("unused")
	private Window window;
	
	public Graphics(Engine e) {
		window = e.getMainWindow();
		
		sprite = new SpriteBatch();
		text = new TextBatch(ResourceManager.getTextureFromResource(DabEngineResources.class, "Fonts/Consolas_font.png").setRegion(16, 16));
		polygon = new PolygonBatch();
		model = new ModelBatch();
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
		else if(cls.isInstance(model)) {
			return cls.cast(model);
		}
		return null;
	}
}
