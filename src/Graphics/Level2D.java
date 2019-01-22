package Graphics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joml.Vector2f;
import org.joml.Vector4f;

import Entities.PhysicsBody.BodyType;
import Graphics.Models.Texture;
import Graphics.Models.Tiles;
import Utils.ResourceManager;

/*
 * In each level file, their is a <header> section and a 
 * the actual level data.
 * Everything between the header is all the meta-data like 
 * width and height and textures to use.
 */

public class Level2D {
	
	public float levelwidth;
	public float levelheight;
	public float tilewidth, tileheight;
	private char spawn_point;
	public Vector2f spawnpos;
	private HashMap<Character, String[]> info = new HashMap<>();
	private char[][] level_info;
	private Tiles[][] tiles;
	private ArrayList<Background> backgrounds = new ArrayList<>();
	private boolean loaded = false;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private String levelname;
	
	public void load(String level) {
		levelname = level;
		loaded = false;
		ArrayList<ArrayList<Character>> raw_info = new ArrayList<>();
		try(BufferedReader in = new BufferedReader(new FileReader(new File(level)));) {
			String s;
			while((s = in.readLine().replace(" ", "")) != null) {
				if(s.equals("[General]")) {
					String[] map = s.split(":");
					String stringval = map[1];
					Float floatval = Float.parseFloat(map[1]);
					switch(map[0]) {
						case "TileWidth":
							tilewidth = floatval;
							break;
						case "TileHeight":
							tileheight = floatval;
							break;
						case "LevelWidth": 
							levelwidth = floatval;
							break;
						case "LevelHeight":
							levelheight = floatval;
							break;
						case "Backgrounds":
							String[] bg = stringval.split(",");
							for(int i = 0; i < bg.length; i++) {
								String[] bg_split = bg[i].split(":");
								switch(bg_split[0]) {
									case "BackgroundFilename":
										backgrounds.add
										(
												new Background() 
												{{
													texture = new Texture(Files.getFileStore(new File(level).toPath()).toString() + bg_split[1]);
												}}
										);
								}
							}
					}
				}
				else if(s.equals("[TileInfo]")) {
					String[] map = s.split(":");
					String[] t_info = map[1].split(",");
					String stringval = map[1];
					Float floatval = Float.parseFloat(map[1]);
					info.put(map[0].charAt(0), t_info);
				}
				else if(s.equals("[TileMap]")) {
					ArrayList<Character> row = new ArrayList<>();
					char[] chars = s.toCharArray();
					for(char c : chars) {
						row.add(c);
					}
					raw_info.add(row);
				}
			}
		} catch(IOException e) {
			LOGGER.log(Level.SEVERE, "Level Load Error", e);
		}
		level_info = convertIntegers(raw_info);
		tiles = new Tiles[level_info.length][level_info[0].length];
		loaded = true;
		LOGGER.log(Level.INFO, "Level '" + level + "' loaded");
	}
	
	public void init(Texture spawn_texture) {
		int height = level_info.length;
		int width = level_info[0].length;
		
		if(tilewidth == 0 && tileheight == 0) {
			tilewidth = levelwidth / (float) width; 
			tileheight = levelheight / (float) height;
		} else if(levelwidth == 0 && levelheight == 0) {
			levelwidth = tilewidth * (float) width;
			levelheight = tileheight * (float) height;
		}
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				for(Map.Entry<Character, String[]> entry : info.entrySet()) {
					if(level_info[y][x] == entry.getKey()) {
						float posx = tilewidth * x;
						float posy = tileheight * y;
						if(entry.getValue()[0].equals("Tile")) {
							Tiles tile = new Tiles(
									ResourceManager.getTexture(entry.getValue()[0]),
									posx,
									posy,
									tilewidth,
									tileheight,
									new Vector4f(1, 1, 1, 1),
									false,
									false,
									BodyType.STATIC);
						}
						if(entry.getValue()[2].equals('1')) {
							tile.setSolid(true);
						} else {
							tile.setSolid(false);
						}
						tiles[y][x] = tile;
					}
				}
				if(level_info[y][x] == spawn_point) {
					float posx = tilewidth * x;
					float posy = tileheight * y;
					spawnpos = new Vector2f(posx, posy);
					/*Tiles tile = new Tiles(
							spawn_texture,
							posx,
							posy,
							tilewidth,
							tileheight,
							new Vector4f(1, 1, 1, 1),
							false,
							false);
					tiles[y][x] = tile;*/
				}
			}
		}
	}
	
	public void render(SpriteBatch batch) {
		for(Background bg : backgrounds) {
			bg.x = 0;
			bg.y = 0;
			bg.width = levelwidth;
			bg.height = levelheight;
			bg.draw(batch);
		}
		for(int y = 0; y < tiles.length; y++) {
			for(int x = 0; x < tiles[0].length; x++) {
				if(tiles[y][x] != null)
					tiles[y][x].render(batch);
			}
		}
	}
	
	public Tiles[][] getTiles() {
		return tiles;
	}
	
	public boolean isLoaded() {
		return loaded;
	}
	
	private char[][] convertIntegers(ArrayList<ArrayList<Character>> integers)
	{
	    char[][] ret = new char[integers.size()][integers.get(0).size()];
	    for (int i=0; i < ret.length; i++)
	    {
	    	for(int j=0; j < ret[0].length; j++)
	    	{
	    		ret[i][j] = integers.get(i).get(j).charValue();
	    	}
	    }
	    return ret;
	}

	public String getLevelName() {
		return levelname.split(".")[0];
	}

	public void setLevelName(String levelname) {
		this.levelname = levelname;
	}
}
