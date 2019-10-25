package DabEngine;

import java.util.ArrayList;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;

import DabEngine.Core.App;
import DabEngine.Core.Engine;
import DabEngine.Graphics.Camera2D;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.OpenGL.Blending;
import DabEngine.Graphics.OpenGL.Viewport.Viewport;
import DabEngine.Input.InputHandler;
import DabEngine.Resources.RenderTarget;
import DabEngine.Resources.ResourceManager;
import DabEngine.Resources.Textures.Texture;
import DabEngine.Resources.Textures.TextureRegion;
import DabEngine.Utils.Color;

public class GraphicsTest extends App 
{

    private static Engine ENGINE = new Engine();
    private Graphics g;
    private class Light
    {
        public Vector3f pos;
        public Vector3f size;
        public Color color;
        public RenderTarget occludersFBO;
        public RenderTarget shadowMap;
    }
    private class Rectangle
    {
        public Vector3f pos, size;
        public Color color;
    }
    private ArrayList<Light> lights = new ArrayList<>();
    private ArrayList<Rectangle> boxes = new ArrayList<>();
    private Camera2D camera;

    {
        TITLE = "test";
        WIDTH = 800;
        HEIGHT = 600;
        MAXIMISED = false;
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        g.dispose();
    }

    @Override
    public void render() 
    {

        GL33.glClearColor(0f, 0f, 0f, 1f);
        GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);

        for(Light l : lights) drawLight(l);

        g.begin();
        g.setBlendMode(Blending.MIX);
        g.setCamera(camera);
        drawOcc(g);
        // g.setTexture(l.occludersFBO.texture[0]);
        // g.drawQuad(new TextureRegion().setUV(0,1,1,0), new Vector3f((WIDTH/2) - l.size.x, 0, 0), new Vector3f(l.occludersFBO.texture[0].getWidth()/2, l.occludersFBO.texture[0].getHeight()/2f, 0), new Vector3f(0), new Vector4f(1,0,0,0), Color.WHITE);
        // g.setTexture(l.shadowMap.texture[0]);
        // g.drawQuad(new TextureRegion().setUV(0,1,1,0), new Vector3f((WIDTH/2) - l.size.x, l.size.y+5, 0), new Vector3f(l.shadowMap.texture[0].getWidth()/2, l.shadowMap.texture[0].getHeight()/2f, 0), new Vector3f(0), new Vector4f(1,0,0,0), Color.WHITE);
        g.end();
        
    }

    private void drawOcc(Graphics g)
    {
        for(Rectangle r : boxes)
        {
            g.drawQuad(r.pos, r.size, new Vector3f(0), new Vector4f(1,0,0,0), r.color);
        }
    }

    private void drawLight(Light l)
    {
        l.occludersFBO.bind();

        GL33.glClearColor(0, 0, 0, 0);
        GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);

        g.begin();
        Camera2D cam = new Camera2D(l.occludersFBO.texture[0].getWidth(), l.occludersFBO.texture[0].getHeight());
        cam.setPosition(l.pos);
        g.setCamera(cam);
        drawOcc(g);
        g.end();

        l.occludersFBO.unbind();

        l.shadowMap.bind();
    
        GL33.glClearColor(0, 0, 0, 0);
        GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);
        g.begin();
        g.setCurrentShader(ResourceManager.INSTANCE.getShader("src/test/resources/pass.glsl|src/test/resources/occluders.glsl", false));
        g.getCurrentShader().setUniform2f("resolution", new Vector2f(l.size.x, l.size.y));

        Camera2D cam2 = new Camera2D(l.shadowMap.texture[0].getWidth(), l.shadowMap.texture[0].getHeight());
        g.setCamera(cam2);

        g.setTexture(l.occludersFBO.texture[0]);
        g.drawQuad(new TextureRegion().setUV(0,1,1,0), new Vector3f(0), new Vector3f(l.size.x/2, 1f/2f, 0), new Vector3f(0), new Vector4f(1,0,0,0), Color.WHITE);

        g.end();

        l.shadowMap.unbind();

        g.begin();
        g.setBlendMode(new Blending(() -> GL33.glBlendFunc(GL33.GL_SRC_ALPHA, GL33.GL_ONE)));
        g.setCurrentShader(ResourceManager.INSTANCE.getShader("src/test/resources/pass.glsl|src/test/resources/shadowmap.glsl", false));
        g.getCurrentShader().setUniform2f("resolution", new Vector2f(l.size.x, l.size.y));
        g.getCurrentShader().setUniform1f("softShadows", 1f);
        g.setTexture(l.shadowMap.texture[0]);
        g.drawQuad(new TextureRegion().setUV(0,0,1,1), new Vector3f(l.pos), new Vector3f(l.size.x/2f, l.size.y/2f, 0), new Vector3f(0), new Vector4f(1,0,0,0), l.color);
        g.end();
    }

    float timer;
    @Override
    public void update() {
        // TODO Auto-generated method stub
        //l.pos.add(0,-1,0);
        timer += 1/ENGINE.TARGET_FPS;
        if(timer > 1)
        {
            if(InputHandler.INSTANCE.isMousePressed(GLFW.GLFW_MOUSE_BUTTON_LEFT))
            {
                lights.add(new Light()
                {
                    {
                        Vector2d mouse = InputHandler.INSTANCE.getMousePos();
                        pos = new Vector3f(camera.screenToWorld(new Vector2f((float)mouse.x, (float)mouse.y), WIDTH, HEIGHT), 0);
                        size = new Vector3f((float)Math.random() * 512);
                        color = new Color(new float[]{
                            (float)Math.random(), (float)Math.random(), (float)Math.random(), 1.0f
                        });
                        occludersFBO = new RenderTarget((int)size.x, (int)size.y, vp, new Texture((int)size.x, (int)size.y, false, false));
                        shadowMap = new RenderTarget((int)size.x, 1, vp, new Texture((int)size.x, 1, false, false));
                    }
                });
            }

            if(InputHandler.INSTANCE.isMousePressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT))
            {
                boxes.add(new Rectangle()
                {
                    {
                        Vector2d mouse =InputHandler.INSTANCE.getMousePos();
                        pos = new Vector3f(camera.screenToWorld(new Vector2f((float)mouse.x, (float)mouse.y), WIDTH, HEIGHT), 0);
                        size = new Vector3f((float)Math.random() * 100, (float)Math.random() * 100, 0);
                        color = new Color(new float[]{
                            (float)Math.random(), (float)Math.random(), (float)Math.random(), 1.0f
                        });
                    }
                });
            }
            timer = 0;
        }
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        g = ENGINE.createGraphics(this);
        ResourceManager.init();
        vp = new Viewport(0, 0, WIDTH, HEIGHT);
        vp.apply();
        camera = new Camera2D(WIDTH, HEIGHT);


    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    public static void main(String[] args) {
        ENGINE.init(new GraphicsTest());
        ENGINE.run();
    }

}