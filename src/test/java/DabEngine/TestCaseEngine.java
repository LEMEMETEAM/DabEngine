package DabEngine;

import static org.junit.Assert.assertTrue;
import static org.lwjgl.glfw.GLFW.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.json.simple.parser.ParseException;
import org.lwjgl.opengl.GL11;

import DabEngine.Cache.ResourceManager;
import DabEngine.Core.App;
import DabEngine.Core.Engine;
import DabEngine.Graphics.Camera2D;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.Batch.Font;
import DabEngine.Graphics.OpenGL.RenderTarget;
import DabEngine.Graphics.OpenGL.Light.Light2D;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Graphics.OpenGL.Textures.TextureLoader;
import DabEngine.Graphics.OpenGL.Viewport.Viewport;
import DabEngine.Input.InputHandler;
import DabEngine.Graphics.Batch.*;
import DabEngine.Utils.Color;
import DabEngine.Utils.Timer;

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
    private RenderTarget rt;
    private Viewport vp;
    private Camera2D cam;
    private Light2D light1;
    private Light2D light2;
    private Shaders light_shaders;

    {
        TITLE = "Test";
        WIDTH = 800;
        HEIGHT = 600;
        MAXIMISED = false;
        hints = new HashMap<>();
    }

    @Override
    public void render() {

            GL11.glClearColor(1,1,1,1.0f);
            // omr.draw(g);
            g.begin(rt);
                g.setCamera(cam);
                g.pushShader(Light2D.LIGHT_SHADER);
                {
                    g.getCurrentShader().setUniform("lights[0].position", light1.pos);
                    g.getCurrentShader().setUniform("lights[0].color", light1.color);
                    g.getCurrentShader().setUniform("ambientStrength", 0.75f);
                    //g.fillRect(0, 0, 1, WIDTH, HEIGHT, 0, 0, 0, Color.RED);
                    g.drawLine(0, 0, 100, 100, 0, 10, Color.RED);
                    g.pushShader(DEFAULT_SHADER);
                    {
                        g.drawText(font, "The Quick Brown Fox Jumped Over The Lazy Dog", 100, 100, 0, Color.BLACK);
                    }
                    g.popShader();
                    g.drawRect(500, 200, 0, 100, 100, 5, Color.RED);
                    g.fillRect(350, 400, 0, 100, 100, 50, 50, rotation, Color.RED);
                    g.drawTexture(t, null, 500, 100, 0, 500, 500, 0, 0, 0, Color.WHITE);
                    g.pushShader(DEFAULT_SHADER);
                    {
                        g.drawText(font, "UPS: " + String.valueOf(ENGINE.UPDATES) + ", FPS: " + String.valueOf(ENGINE.FRAMES), 0, 24, 1, Color.BLACK);
                    }
                    g.popShader();
                }
                g.popShader();
            g.end();
    }

    @Override
    public void update() {
        //rotation+=(float)Timer.getDelta() * 0.25f;
        Vector2d m = InputHandler.INSTANCE.getMousePos();
        Vector2f mScreen = cam.screenToWorld(new Vector2f((float)m.x, (float)m.y), vp.width, vp.height);
        light1.pos.x = mScreen.x;
        light1.pos.y = mScreen.y;
        if(InputHandler.INSTANCE.isKeyPressed(GLFW_KEY_MINUS)){
            light1.pos.z -= 0.1;
        }
        if(InputHandler.INSTANCE.isKeyPressed(GLFW_KEY_EQUAL)){
            light1.pos.z += 0.1;
        }
        cam.rotate(new Vector3f(0,1,0), (float)Math.toRadians(rotation));
    }

    @Override
    public void init() {

        fullscreenOnMaximise = false;
        
        g = ENGINE.createGraphics(this);

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

        vp = new Viewport(0, 0, WIDTH, HEIGHT);
        vp.apply();

        rt = new RenderTarget(WIDTH, HEIGHT, vp);
        Shaders fboShader = new Shaders(
            App.class.getResourceAsStream("/Shaders/defaultFBO.vs"),
            App.class.getResourceAsStream("/Shaders/defaultFBO.fs"));
        rt.pushShader(fboShader);

        cam = new Camera2D(WIDTH, HEIGHT);

        

        light1 = new Light2D(new Vector3f(400f, 300, 0.5f), 0, new Vector3f(1,0,1));

    }

    public static void main(String[] args) {
        ENGINE.init(new TestCaseEngine());
        ENGINE.run();
    }

    @Override
    public void resize(int width, int height) {
        vp.update(width, height);
        vp.apply();
    }
}