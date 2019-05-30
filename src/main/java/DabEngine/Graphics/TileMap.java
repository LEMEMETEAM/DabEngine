package DabEngine.Graphics;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TileMap {

    private Document doc;
    private class TileInfo {
        public int width, height;
        public int tileWidth, tileHeight;
        public boolean infinite;
    }
    public TileInfo info = new TileInfo();
    public lay

    public TileMap(File xmlFile){
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        DocumentBuilder b = fac.newDocumentBuilder();
        doc = b.parse(xmlFile);

        Element mapRoot = doc.getDocumentElement();
        info.width = Integer.valueOf(mapRoot.getAttribute("width"));
        info.height = Integer.valueOf(mapRoot.getAttribute("height"));
        info.tileWidth = Integer.valueOf(mapRoot.getAttribute("tileWidth"));
        info.tileHeight = Integer.valueOf(mapRoot.getAttribute("tileHeight"));
        boolean b;
        int att = Integer.valueOf(mapRoot.getAttribute("infinite"));
        if(att == 0){
            b = false;
        } else {
            b = true;
        }
        info.infinite = b;

        NodeList layers = doc.getElementsByTag("layer");
        for(int i = 0; i < layers.getLength(); i++){
        }
    }
}

class Layers {
    public ArrayList<MapObject> mObjs;
}
