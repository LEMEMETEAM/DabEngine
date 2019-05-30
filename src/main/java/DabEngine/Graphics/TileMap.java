package DabEngine.Graphics;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TileMap {

    private Document doc;

    public TileMap(File xmlFile){
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        DocumentBuilder b = fac.newDocumentBuilder();
        doc = b.parse(xmlFile);

        Element mapRoot = doc.getDocumentElement();

        
    }
}