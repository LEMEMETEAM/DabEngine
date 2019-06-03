package DabEngine;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;

import org.json.simple.parser.ParseException;
import org.lwjgl.opengl.GL11;

import DabEngine.Core.App;
import DabEngine.Core.Engine;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.OrthagonalMapRenderer;
import DabEngine.Graphics.ProjectionMatrix;
import DabEngine.Graphics.TileMap;
import DabEngine.Graphics.Batch.Font;
import DabEngine.Graphics.Batch.SpriteBatch;
import DabEngine.Graphics.Batch.TextBatch;
import DabEngine.Graphics.Models.Texture;

public class TestCaseEngine extends App {
    private static final Engine ENGINE = new Engine();
    private Graphics g;
    private TileMap map = null;
    private OrthagonalMapRenderer omr = null;
    private Font font;

    {
        TITLE = "Test";
        WIDTH = 800;
        HEIGHT = 600;
        fullscreen = false;
        hints = new HashMap<>();
    }

    @Override
    public void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        //omr.draw(g);

        TextBatch t = g.getBatch(TextBatch.class);
        t.begin();
            t.draw(font, "The Quick Brown Fox Jumped Over The Lazy Dog", 100, 100, 1, 1, 1, 1);
            //t.draw("!\"£$%^&*()|\\,.<>/?'@;:#~{{}}`¬¦_+abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ", 200, 200, 2, 3, 1, 0.5F, 1, 1);
        t.end();
    }

    @Override
    public void update() {

    }

    @Override
    public void init() {
        ProjectionMatrix.createProjectionMatrix2D(0, WIDTH, HEIGHT, 0);
        g = ENGINE.createGraphics();

        try {
            map = new TileMap("C:/Users/B/Documents/DabEngine/src/test/resources/", "untitled..json");
        } catch (ParseException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        omr = new OrthagonalMapRenderer(map);

        font = new Font("src/test/resources/OpenSans-Regular.ttf", 48, 3);
    }

    public static void main(String[] args) {
        ENGINE.init(new TestCaseEngine());
        ENGINE.run();
    }
}