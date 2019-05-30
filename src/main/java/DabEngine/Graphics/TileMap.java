package DabEngine.Graphics;

import java.io.File;
import java.io.FileReader;
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

    public class TileInfo {
        public int width, height;
        public int tileWidth, tileHeight;
        public boolean infinite;
    }

    public TileInfo info = new TileInfo();
    public HashMap<Integer, Texture> tilesets = new HashMap<>();
    public ArrayList<MapLayer> layers = new ArrayList<MapLayer>();

    public TileMap(String dir, String jsonFile) throws ParseException, IOException {
        JSONObject jo = ((JSONObject)new JSONParser().parse(new FileReader(new File(dir + jsonFile))));
        //set info
        info.width = ((Long)jo.get("width")).intValue();
        info.height = ((Long)jo.get("height")).intValue();
        info.tileWidth = ((Long)jo.get("tilewidth")).intValue();
        info.tileHeight = ((Long)jo.get("tileheight")).intValue();
        info.infinite = (boolean)jo.get("infinite");

        //get tileset
        JSONArray tiles = (JSONArray)jo.get("tilesets");
        for(int i = 0; i < tiles.size(); i++){
            JSONObject obj = (JSONObject)tiles.get(i);
            int imagewidth = ((Long)obj.get("imagewidth")).intValue(), imageheight = ((Long)obj.get("imageheight")).intValue(), tilewidth = ((Long)obj.get("tilewidth")).intValue(), tileheight = ((Long)obj.get("tileheight")).intValue();
            Texture tex = new Texture(new File(dir + (String)obj.get("image")), imagewidth/tilewidth, imageheight/tileheight);
            tilesets.put(((Long)obj.get("firstgid")).intValue(), tex);
        }

        //get layers
        JSONArray layersA = (JSONArray)jo.get("layers");
        for(int i = 0; i < layersA.size(); i++){
            layers.add(new MapLayer());
            JSONObject obj = (JSONObject)layersA.get(i);
            JSONArray data = (JSONArray)obj.get("data");
            for(int j = 0; j < data.size(); j++){
                int id = ((Long)data.get(j)).intValue();
                TileMapObject t = new TileMapObject();
                if(id == 0){
                    layers.get(i).mOBjs.add(null);
                }
                else{
                    var tMOBJ = new TileMapObject();
                    tMOBJ.tileNum = id;
                    Texture tex = getTextureFromId(id);
                    tMOBJ.widthInTiles = (tex.width/tex.getRegion().tileNomX) / info.tileWidth;
                    tMOBJ.heightInTiles= (tex.height/tex.getRegion().tileNomY) / info.tileHeight;

                    layers.get(i).mOBjs.add(tMOBJ);
                }
            }
        }
    }

    public Texture getTile(int layer, int x, int y){
        int index = x + info.width*y;
        TileMapObject t = (TileMapObject)layers.get(layer).mOBjs.get(index);
        if(t == null){
            return null;
        }

        Texture tex = (Texture) getTextureFromId(t.tileNum).clone();
        tex.getRegion().setTile(t.tileNum);
        return tex;
    }

    public int getFinalTileWidth(int layer, int x, int y){
        int index = x + info.width*y;
        TileMapObject t = (TileMapObject)layers.get(layer).mOBjs.get(index);
        if(t == null){
            return 0;
        }

        return info.tileWidth * t.widthInTiles;
    }

    public int getFinalTileHeight(int layer, int x, int y){
        int index = x + info.width*y;
        TileMapObject t = (TileMapObject)layers.get(layer).mOBjs.get(index);
        if(t == null){
            return 0;
        }

        return info.tileHeight * t.heightInTiles;
    }

    public Texture getTextureFromId(int id){
        Texture tex = null;
        if(tilesets.size() < 2){
            tex = tilesets.get(1);
        }
        else{
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
        }
        return tex;
    }
}

class MapLayer{
    public ArrayList<MapObject> mOBjs = new ArrayList<>();
}

class MapObject {
    public int widthInTiles, heightInTiles;
}

class TileMapObject extends MapObject {
    public int tileNum;
}
