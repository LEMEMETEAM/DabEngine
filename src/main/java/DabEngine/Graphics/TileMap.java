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

import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Graphics.OpenGL.Textures.TextureLoader;
import DabEngine.Graphics.OpenGL.Textures.TextureRegion;
import DabEngine.Utils.Pair;

public class TileMap {

    /**
     * Strcutthat holds info for the tilemap
     */
    public class TileInfo {
        public int width, height;
        public int tileWidth, tileHeight;
        public boolean infinite;
    }

    public class MapLayer{
        public ArrayList<MapObject> mOBjs = new ArrayList<>();
        public String type;
        public String name;
        public HashMap<String, Pair<Class<?>, Object>> props = new HashMap<>();
    }
    
    public class MapObject {
        public int widthInTiles, heightInTiles;
        public boolean draw;
        public HashMap<String, Pair<Class<?>, Object>> props = new HashMap<>();
    }
    
    public class RectangleMapObject extends MapObject {
        public float x, y, width, height, rotation;
    }
    
    public class SpawnerMapObject extends RectangleMapObject {
    
    }
    
    public class PointMapObject extends MapObject {
        public float x, y;
    }
    
    public class TileMapObject extends MapObject {
        public int tileNum;
    }

    public TileInfo info = new TileInfo();
    public HashMap<Integer, Pair<Texture, TextureRegion>> tilesets = new HashMap<>();
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
            TextureLoader loader = new TextureLoader(new File(dir + (String)obj.get("image")));
            Texture tex = new Texture(loader.pixels, loader.width, loader.height, Texture.Parameters.LINEAR);
            TextureRegion r = new TextureRegion(imagewidth / tilewidth, imageheight / tileheight);
            tilesets.put(((Long)obj.get("firstgid")).intValue(), new Pair<>(tex, r));
        }

        //get tile layers
        JSONArray layersA = (JSONArray)jo.get("layers");
        for(int i = 0; i < layersA.size(); i++){
            MapLayer l = new MapLayer();
            JSONObject obj = (JSONObject)layersA.get(i);
            l.type = (String)obj.get("type");
            l.name = (String)obj.get("name");
            JSONArray lprops = (JSONArray)obj.get("properties");
            if(lprops != null){
                for(int k = 0; k < lprops.size(); k++){
                    JSONObject prop = (JSONObject)lprops.get(k);
                    Class<?> type = null;
                    switch((String)prop.get("type")){
                        case "bool":
                            type = Boolean.class;
                            break;
                        case "int":
                            type = Integer.class;
                            break;
                        case "float":
                            type = Float.class;
                            break;
                        case "string":
                            type = String.class;
                            break;
                    }
                    l.props.put((String)prop.get("name"), new Pair<>(type, prop.get("value")));
                }
            }
            layers.add(l);
            switch(l.type){
                case "tilelayer":
                    JSONArray data = (JSONArray)obj.get("data");
                    for(int j = 0; j < data.size(); j++){
                        int id = ((Long)data.get(j)).intValue();
                        if(id == 0){
                            layers.get(i).mOBjs.add(null);
                        }
                        else{
                            var tMOBJ = new TileMapObject();
                            tMOBJ.tileNum = id;
                            var pair = getTextureFromId(id);
                            tMOBJ.widthInTiles = (pair.left.getWidth()/pair.right.tileNomX) / info.tileWidth;
                            tMOBJ.heightInTiles= (pair.left.getHeight()/pair.right.tileNomY) / info.tileHeight;
                            JSONArray props = (JSONArray)obj.get("properties");
                            if(props != null){
                                for(int k = 0; k < props.size(); k++){
                                    JSONObject prop = (JSONObject)props.get(k);
                                    Class<?> type = null;
                                    switch((String)prop.get("type")){
                                        case "bool":
                                            type = Boolean.class;
                                            break;
                                        case "int":
                                            type = Integer.class;
                                            break;
                                        case "float":
                                            type = Float.class;
                                            break;
                                        case "string":
                                            type = String.class;
                                            break;
                                    }
                                    tMOBJ.props.put((String)prop.get("name"), new Pair<>(type, prop.get("value")));
                                }
                            }
        
                            layers.get(i).mOBjs.add(tMOBJ);
                        }
                    }
                    break;
                case "objectgroup":
                    JSONArray objects = (JSONArray)obj.get("objects");
                    for(int j = 0; j < objects.size(); j++){
                        JSONObject o = (JSONObject)objects.get(j);
                        JSONArray props = (JSONArray)obj.get("properties");
                        switch((String)o.get("type")){
                            case "rect":
                            case "Rect":
                            case "rectangle":
                            case "Rectangle":
                            case "R":
                            case "r":
                                RectangleMapObject r = new RectangleMapObject();
                                r.x = ((Long)o.get("x")).intValue();
                                r.y = ((Long)o.get("y")).intValue();
                                r.width = ((Long)o.get("width")).intValue();
                                r.height = ((Long)o.get("height")).intValue();
                                r.draw = (boolean)o.get("visible");

                                if(props != null){
                                    for(int k = 0; k < props.size(); k++){
                                        JSONObject prop = (JSONObject)props.get(k);
                                        Class<?> type = null;
                                        switch((String)prop.get("type")){
                                            case "bool":
                                                type = Boolean.class;
                                                break;
                                            case "int":
                                                type = Integer.class;
                                                break;
                                            case "float":
                                                type = Float.class;
                                                break;
                                            case "string":
                                                type = String.class;
                                                break;
                                        }
                                        r.props.put((String)prop.get("name"), new Pair<>(type, prop.get("value")));
                                    }
                                }

                                layers.get(i).mOBjs.add(r);
                                break;
                            
                            case "p":
                            case "P":
                            case "point":
                            case "Point":
                                PointMapObject p = new PointMapObject();
                                p.x = ((Long)o.get("x")).intValue();
                                p.y = ((Long)o.get("y")).intValue();
                                p.draw = (boolean)o.get("visible");

                                if(props != null){
                                    for(int k = 0; k < props.size(); k++){
                                        JSONObject prop = (JSONObject)props.get(k);
                                        Class<?> type = null;
                                        switch((String)prop.get("type")){
                                            case "bool":
                                                type = Boolean.class;
                                                break;
                                            case "int":
                                                type = Integer.class;
                                                break;
                                            case "float":
                                                type = Float.class;
                                                break;
                                            case "string":
                                                type = String.class;
                                                break;
                                        }
                                        p.props.put((String)prop.get("name"), new Pair<>(type, prop.get("value")));
                                    }
                                }

                                layers.get(i).mOBjs.add(p);
                                break;

                            case "s":
                            case "spawn":
                            case "SPAWN":
                            case "SPAWN_POINT":
                            case "spawn_point":
                            case "spawnP":
                                SpawnerMapObject s = new SpawnerMapObject();
                                s.x = ((Long)o.get("x")).intValue();
                                s.y = ((Long)o.get("y")).intValue();
                                s.width = ((Long)o.get("width")).intValue();
                                s.height = ((Long)o.get("height")).intValue();
                                s.draw = (boolean)o.get("visible");

                                if(props != null){
                                    for(int k = 0; k < props.size(); k++){
                                        JSONObject prop = (JSONObject)props.get(k);
                                        Class<?> type = null;
                                        switch((String)prop.get("type")){
                                            case "bool":
                                                type = Boolean.class;
                                                break;
                                            case "int":
                                                type = Integer.class;
                                                break;
                                            case "float":
                                                type = Float.class;
                                                break;
                                            case "string":
                                                type = String.class;
                                                break;
                                        }
                                        s.props.put((String)prop.get("name"), new Pair<>(type, prop.get("value")));
                                    }
                                }

                                layers.get(i).mOBjs.add(s);
                                break;
                            
                            default:
                                MapObject mo = new MapObject();

                                if(props != null){
                                    for(int k = 0; k < props.size(); k++){
                                        JSONObject prop = (JSONObject)props.get(k);
                                        Class<?> type = null;
                                        switch((String)prop.get("type")){
                                            case "bool":
                                                type = Boolean.class;
                                                break;
                                            case "int":
                                                type = Integer.class;
                                                break;
                                            case "float":
                                                type = Float.class;
                                                break;
                                            case "string":
                                                type = String.class;
                                                break;
                                        }
                                        mo.props.put((String)prop.get("name"), new Pair<>(type, prop.get("value")));
                                    }
                                }

                                layers.get(i).mOBjs.add(mo);
                                break;
                        }
                    }
            }
            
        }
    }

    public Pair<Texture, TextureRegion> getTile(MapLayer layer, int x, int y){
        int index = x + info.width*y;
        TileMapObject t = (TileMapObject)layer.mOBjs.get(index);
        if(t == null){
            return null;
        }

        Pair<Texture, TextureRegion> pair = (Pair<Texture, TextureRegion>) getTextureFromId(t.tileNum);
        pair.right.setTile(t.tileNum);
        return pair;
    }

    public int getFinalTileWidth(MapLayer layer, int x, int y){
        int index = x + info.width*y;
        TileMapObject t = (TileMapObject)layer.mOBjs.get(index);
        if(t == null){
            return 0;
        }

        return info.tileWidth * t.widthInTiles;
    }

    public int getFinalTileHeight(MapLayer layer, int x, int y){
        int index = x + info.width*y;
        TileMapObject t = (TileMapObject)layer.mOBjs.get(index);
        if(t == null){
            return 0;
        }

        return info.tileHeight * t.heightInTiles;
    }

    public Pair<Texture, TextureRegion> getTextureFromId(int id){
        Pair<Texture, TextureRegion> tex = null;
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
