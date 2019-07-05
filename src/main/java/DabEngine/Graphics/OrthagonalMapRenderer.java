package DabEngine.Graphics;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

import DabEngine.Graphics.Batch.Polygon;
import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Graphics.OpenGL.Textures.TextureRegion;
import DabEngine.Graphics.TileMap.MapLayer;
import DabEngine.Utils.Color;
import DabEngine.Utils.Pair;
import DabEngine.Utils.Primitives.Primitives;

public class OrthagonalMapRenderer {

    private TileMap map;

    public OrthagonalMapRenderer(TileMap map) {
        this.map = map;
    }

    public void draw(Graphics g){
        int i =0;
        for(MapLayer layer : map.layers){
            i++;
            switch (layer.type){
                case "tilelayer":
                    //TODO: add render target stuff for effects on tile layer
                    g.begin(null);
                        for(int y = 0; y < map.info.height; y++){
                            for(int x = 0; x < map.info.width; x++){
                                Pair<Texture, TextureRegion> t = map.getTile(layer, x, y);
                                if(t != null)
                                    g.drawTexture(t.left, t.right, x * map.info.tileWidth, y * map.info.tileHeight, i, map.getFinalTileWidth(layer, x, y), map.getFinalTileHeight(layer, x, y), 0, 0, 0, Color.WHITE);
                            }
                        }
                    g.end();
                    break;
                
                /* case "objectgroup":
                    PolygonBatch poly = g.getBatch(PolygonBatch.class);
                    poly.begin();
                        for(MapObject o : layer.mOBjs){
                            if(o != null){
                                if(o.draw){
                                    if(o instanceof RectangleMapObject){
                                        RectangleMapObject r = (RectangleMapObject)o;
                                        poly.setPrimitiveType(GL11.GL_TRIANGLES);
                                        Polygon polygon = new Polygon(
                                            new int[] {
                                                0,1,2,
                                                2,3,0
                                            },
                                            new Vector2f[] {
                                                new Vector2f(0, 0),
                                                new Vector2f(0, 1),
                                                new Vector2f(1, 1),
                                                new Vector2f(1, 0)
                                            }
                                        );
                                        poly.draw(polygon, r.x, r.y, r.width, r.height, 0, 0, 0);
                                    }
                                    else if(o instanceof PointMapObject){
                                        PointMapObject p = (PointMapObject)o;
                                        poly.setPrimitiveType(GL11.GL_POINTS);
                                        poly.draw(Primitives.POINT.generate(), p.x, p.y, 1, 1, 0, 0, 0);
                                    }
                                }
                            }
                        }
                    poly.end();
                    break; */
            }
        }
    }
}