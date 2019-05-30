package DabEngine.Graphics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import DabEngine.Graphics.Models.Texture;

public class TileMap {

    private class TileInfo {
        public int width, height;
        public int tileWidth, tileHeight;
        public boolean infinite;
    }

    public TileInfo info = new TileInfo();
    public HashMap<Integer, Texture> tilesets = new HashMap<>();
    public MapLayers layers = new MapLayers();

    public TileMap(String jsonFile) throws ParseException, IOException {
        JSONObject jo = ((JSONObject)new JSONParser().parse(jsonFile));

        //set info
        info.width = (int)jo.get("width");
        info.height = (int)jo.get("height");
        info.tileWidth = (int)jo.get("tilewidth");
        info.tileHeight = (int)jo.get("tileheight");
        info.infinite = (boolean)jo.get("infinite");

        //get tileset
        JSONArray tiles = (JSONArray)jo.get("tilesets");
        for(int i = 0; i < tiles.size(); i++){
            JSONObject obj = (JSONObject)tiles.get(i);
            int imagewidth = (int)obj.get("imagewidth"), imageheight = (int)obj.get("imageheight"), tilewidth = (int)obj.get("tilewidth"), tileheight = (int)obj.get("imageheight");
            Texture tex = new Texture(new File((String)obj.get("image")), imagewidth/tilewidth, imageheight/tileheight);
            tilesets.put((int)obj.get("firstgid"), tex);
        }

        //get layers
        JSONArray layersA = (JSONArray)jo.get("layers");
        for(int i = 0; i < layersA.size(); i++){
            JSONObject obj = (JSONObject)layersA.get(i);
            JSONArray data = (JSONArray)obj.get("data");
            for(int j = 0; j < data.size(); j++){
                int id = (int)data.get(j);
                TileMapObject t = new TileMapObject();
                if(id == 0){
                    layers.mOBjs.add(null);
                }
                else{
                    var tMOBJ = new TileMapObject();
                    tMOBJ.tileNum = id;
                    Texture tex = null;
                    for(int k = 1; k < tilesets.size(); k++){
                        int idKey = (int)tilesets.keySet().toArray()[k];
                        if(id < idKey){
                            tex = tilesets.get(idKey);
                            break;
                        }
                        else{
                            continue;
                        }
                    }
                    tMOBJ.widthInTiles = (tex.width/tex.getRegion().tileNomX) / info.tileWidth;
                    tMOBJ.heightInTiles= (tex.height/tex.getRegion().tileNomY) / info.tileHeight;

                    layers.mOBjs.add(tMOBJ);
                }
            }
        }
    }
}

class MapLayers{
    public ArrayList<MapObject> mOBjs = new ArrayList<>();
}

class MapObject {
    public int widthInTiles, heightInTiles;
}

class TileMapObject extends MapObject {
    public int tileNum;
}
