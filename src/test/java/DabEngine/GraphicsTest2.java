package DabEngine;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import DabEngine.Core.App;
import DabEngine.Core.Engine;
import DabEngine.Graphics.Camera2D;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.OpenGL.Viewport.Viewport;
import DabEngine.Resources.RenderTarget;
import DabEngine.Resources.ResourceManager;
import DabEngine.Resources.Textures.Texture;
import DabEngine.Resources.Textures.TextureRegion;
import DabEngine.Utils.Color;

public class GraphicsTest2 extends App 
{

    private static Engine ENGINE = new Engine();
    private Graphics g;
    private class Light
    {
        public Vector3f pos;
        public Vector3f size;
        public RenderTarget occludersFBO;
        public RenderTarget shadowMap;
    }
    private Light l;

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

        GL11.glClearColor(0.25f, 0.25f, 0.25f, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        drawLight(l);

        g.begin();
        drawOcc(g);
        // g.setTexture(l.occludersFBO.texture[0]);
        // g.drawQuad(new TextureRegion().setUV(0,1,1,0), new Vector3f((WIDTH/2) - l.size.x, 0, 0), new Vector3f(l.occludersFBO.texture[0].getWidth()/2, l.occludersFBO.texture[0].getHeight()/2f, 0), new Vector3f(0), new Vector4f(1,0,0,0), Color.WHITE);
        // g.setTexture(l.shadowMap.texture[0]);
        // g.drawQuad(new TextureRegion().setUV(0,1,1,0), new Vector3f((WIDTH/2) - l.size.x, l.size.y+5, 0), new Vector3f(l.shadowMap.texture[0].getWidth()/2, l.shadowMap.texture[0].getHeight()/2f, 0), new Vector3f(0), new Vector4f(1,0,0,0), Color.WHITE);
        g.end();
        
    }

    private void drawOcc(Graphics g)
    {
        g.drawQuad(new Vector3f(-30, -10, 0), new Vector3f(15, 15, 0), new Vector3f(0), new Vector4f(1,0,0,0), Color.RED);
        g.drawQuad(new Vector3f(-64, 185, 0), new Vector3f(10, 10, 0), new Vector3f(0), new Vector4f(1,0,0,0), Color.GREEN);
        g.drawQuad(new Vector3f(-37, -94, 0), new Vector3f(38, 38, 0), new Vector3f(0), new Vector4f(1,0,0,0), Color.BLUE);
    }

    private void drawLight(Light l)
    {
        l.occludersFBO.bind();

        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        g.begin();
        Camera2D cam = new Camera2D(l.occludersFBO.texture[0].getWidth(), l.occludersFBO.texture[0].getHeight());
        cam.setPosition(l.pos);
        g.setCamera(cam);
        drawOcc(g);
        g.end();

        l.occludersFBO.unbind();

        l.shadowMap.bind();
    
        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        g.begin();

        Camera2D cam2 = new Camera2D(l.shadowMap.texture[0].getWidth(), l.shadowMap.texture[0].getHeight());
        g.setCamera(cam2);

        g.setTexture(l.occludersFBO.texture[0]);
        int scalex = 1, scaley = 1;
        g.drawQuad(new TextureRegion().setUV(0,1,1,0), new Vector3f(0), new Vector3f((l.size.x/2), (l.size.y/2f), 0), new Vector3f(0), new Vector4f(1,0,0,0), Color.WHITE);
        for(int i = 0; i < 3; i++)
        {
            g.drawQuad(new TextureRegion().setUV(0,1,1,0), new Vector3f(0), new Vector3f((l.size.x/2)+(scalex*=2), (l.size.y/2f)+(scaley*=2), 0), new Vector3f(0), new Vector4f(1,0,0,0), Color.WHITE);
        }

        g.end();

        l.shadowMap.unbind();

        g.begin();
        g.setCurrentShader(ResourceManager.INSTANCE.getShader("src/test/resources/pass.glsl|src/test/resources/occluders2.glsl", false));
        g.getCurrentShader().setUniform2f("resolution", new Vector2f(l.size.x, l.size.y));
        g.setTexture(l.shadowMap.texture[0]);
        g.drawQuad(new TextureRegion().setUV(0,1,1,0), new Vector3f(l.pos), new Vector3f(l.size.x/2f, l.size.y/2f, 0), new Vector3f(0), new Vector4f(1,0,0,0), Color.WHITE);
        g.end();
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        //l.pos.add(0,-1,0);
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        g = ENGINE.createGraphics(this);
        ResourceManager.init();
        vp = new Viewport(0, 0, WIDTH, HEIGHT);
        vp.apply();

        l = new Light(){
            {
                pos = new Vector3f(0);
                size = new Vector3f(256, 256, 0);
                occludersFBO = new RenderTarget((int)size.x, (int)size.y, vp, new Texture((int)size.x, (int)size.y, false, false));
                shadowMap = new RenderTarget((int)size.x, (int)size.y, vp, new Texture((int)size.x, (int)size.y, false, false));
                //shadowMap.texture[0].setWrapMode(TextureWrap.REPEAT);
            }
        };
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    public static void main(String[] args) {
        ENGINE.init(new GraphicsTest2());
        ENGINE.run();
    }

}