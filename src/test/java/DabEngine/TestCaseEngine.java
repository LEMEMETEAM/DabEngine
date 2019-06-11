package DabEngine;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.json.simple.parser.ParseException;
import org.lwjgl.opengl.GL11;

import DabEngine.Cache.ResourceManager;
import DabEngine.Core.App;
import DabEngine.Core.Engine;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.ProjectionMatrix;
import DabEngine.Graphics.Batch.Font;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Graphics.OpenGL.Textures.TextureLoader;
import DabEngine.Graphics.Batch.*;
import DabEngine.Utils.Colors;

public class TestCaseEngine extends App {
    private static final Engine ENGINE = new Engine();
    private Graphics g;
    //private OrthagonalMapRenderer omr = null;
    private Font font;
    private QuadBatch u;
    //private TextBatch text;
    private Texture t;
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
        GL11.glClearColor(1.0f,1.0f,1.0f,1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        // omr.draw(g);
        g.begin(null);
            g.drawLine(0, 0, 100, 100, 10, Colors.RED.color);
            g.pushShader(DEFAULT_SHADER);
            g.drawText(font, "The Quick Brown Fox Jumped Over The Lazy Dog", 100, 100, Colors.BLACK.color);
            g.popShader();
            g.drawRect(500, 200, 100, 100, 5, Colors.RED.color);
            g.fillRect(350, 400, 100, 100, 50, 50, rotation, Colors.RED.color);
            g.drawTexture(t, 500, 100, 500, 500, 0, 0, 0, Colors.WHITE.color);
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
            try{
            TextureLoader loader = new TextureLoader(new File("src/test/resources/Tiles_64x64.png"));
            t =new Texture(loader.pixels, loader.width, loader.height, Texture.Parameters.LINEAR);
            }catch(IOException e){
                e.printStackTrace();
            }

    }

    public static void main(String[] args) {
        ENGINE.init(new TestCaseEngine());
        ENGINE.run();
    }
}