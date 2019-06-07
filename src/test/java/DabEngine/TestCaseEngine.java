package DabEngine;

import static org.junit.Assert.assertTrue;

import java.io.File;
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
import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.Batch.*;
import DabEngine.Utils.Colors;

public class TestCaseEngine extends App {
    private static final Engine ENGINE = new Engine();
    private Graphics g;
    private TileMap map = null;
    //private OrthagonalMapRenderer omr = null;
    private Font font;
    private QuadBatch u;
    //private TextBatch text;
    //private Texture t;
    public static Shaders DEFAULT_SHADER;
    private float rotation = 0;

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

        // omr.draw(g);
        g.begin();
            g.drawLine(0, 0, 100, 100, 10);
            g.pushShader(DEFAULT_SHADER);
            g.drawText(font, "The Quick Brown Fox Jumped Over The Lazy Dog", 100, 100, Colors.WHITE.color);
            g.popShader();
            g.drawRect(500, 200, 100, 100, 5);
            g.fillRect(350, 400, 100, 100, 50, 50, rotation, Colors.RED.color);
        g.end();
    }

    @Override
    public void update() {
        rotation+=10;
    }

    @Override
    public void init() {
        ProjectionMatrix.createProjectionMatrix2D(0, WIDTH, HEIGHT, 0);
        
        u = new QuadBatch(QuadBatch.DEFAULT_SHADER);
        g = ENGINE.createGraphics();

            //map = new TileMap("C:/Users/B/Documents/DabEngine/src/test/resources/", "untitled..json");

        //omr = new OrthagonalMapRenderer(map);

        font = new Font("src/test/resources/OpenSans-Regular.ttf", 48, 3);

        //text = new TextBatch(TextBatch.DEFAULT_SHADER);

        DEFAULT_SHADER = new Shaders(
			App.class.getResourceAsStream("/Shaders/textDefault.vs"),
			App.class.getResourceAsStream("/Shaders/text.fs"));
    }

    public static void main(String[] args) {
        ENGINE.init(new TestCaseEngine());
        ENGINE.run();
    }
}