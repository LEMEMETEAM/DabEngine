package Graphics;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.joml.Vector2f;
import org.joml.Vector4f;

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
	private char spawn_point;
	public Vector2f spawnpos;
	private HashMap<Character, String[]> info = new HashMap<>();
	private char[][] level_info;
	private Tiles[][] tiles;
	private boolean loaded = false;
	
	public void load(String level) {
		loaded = false;
		BufferedReader in;
		boolean start_of_tag = false;
		boolean end_of_tag = false;
		String tag_start_name = "";
		ArrayList<ArrayList<Character>> raw_info = new ArrayList<>();
		try {
			in = new BufferedReader(new FileReader(new File(level)));
			String s;
			while((s = in.readLine()) != null) {
				if(start_of_tag) {
					if(s.equals("</" + tag_start_name + ">")) {
						start_of_tag = false;
						end_of_tag = true;
						continue;
					}
					if(s.contains("=")) {
						s = s.replace(";", "").strip();
						String[] split = s.split(" = ", 2);
						if(split[0].contains("width")) {
							levelwidth = Float.parseFloat(split[1]);
						} else if(split[0].contains("height")) {
							levelheight = Float.parseFloat(split[1]);
						} else if(split[0].contains("spawn") && split[0].contains("point")) {
							spawn_point = split[1].strip().charAt(0);
						} else {
							String[] map = split[1].split(", ", 2);
							info.put(split[0].charAt(0), map);
						}
					}
				} else {
					start_of_tag = s.startsWith("<") && s.endsWith(">");
					tag_start_name = s.replace("<", "").replace(">", "");
				}
				if(end_of_tag) {
					char[] chars = s.toCharArray();
					ArrayList<Character> rows = new ArrayList<>();
					for(int i = 0; i < chars.length; i++) {
						rows.add(chars[i]);
					}
					raw_info.add(rows);
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
			System.err.println("Level load error");
		}
		level_info = convertIntegers(raw_info);
		tiles = new Tiles[level_info.length][level_info[0].length];
		loaded = true;
		System.out.println("Level '" + level + "' loaded");
	}
	
	public void init(Texture spawn_texture) {
		int height = level_info.length;
		int width = level_info[0].length;
		
		float tilewidth = levelwidth / (float) width, tileheight = levelheight / (float) height;
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				for(Map.Entry<Character, String[]> entry : info.entrySet()) {
					if(level_info[y][x] == entry.getKey()) {
						float posx = tilewidth * x;
						float posy = tileheight * y;
						Tiles tile = new Tiles(
								ResourceManager.getTexture(entry.getValue()[0]),
								posx,
								posy,
								tilewidth,
								tileheight,
								new Vector4f(1, 1, 1, 1),
								false,
								false);
						if(entry.getValue()[1].equals("solid")) {
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
					Tiles tile = new Tiles(
							spawn_texture,
							posx,
							posy,
							tilewidth,
							tileheight,
							new Vector4f(1, 1, 1, 1),
							false,
							false);
					tiles[y][x] = tile;
				}
			}
		}
	}
	
	public void render(SpriteBatch batch) {
		for(int y = 0; y < tiles.length; y++) {
			for(int x = 0; x < tiles[0].length; x++)
			tiles[y][x].render(batch);
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
}
