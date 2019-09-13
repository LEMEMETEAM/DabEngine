package DabEngine;

import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.Test;

import DabEngine.Cache.ResourceManager;
import DabEngine.Core.App;
import DabEngine.Core.Engine;
import DabEngine.Graphics.Camera3D;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.Models.Mesh;
import DabEngine.Graphics.Models.MeshLoader;
import DabEngine.Graphics.Models.Model;
import DabEngine.Graphics.OpenGL.Blending;
import DabEngine.Graphics.OpenGL.RenderTarget;
import DabEngine.Graphics.OpenGL.Light.Light;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Graphics.OpenGL.Textures.TextureRegion;
import DabEngine.Graphics.OpenGL.Textures.Texture.Parameters;
import DabEngine.Graphics.OpenGL.Viewport.Viewport;
import DabEngine.Input.InputHandler;
import DabEngine.Utils.Color;
import DabEngine.Utils.Pair;
import DabEngine.Utils.Timer;
import DabEngine.Utils.Primitives.Cube;

import static org.lwjgl.opengl.GL33.*;

import java.io.File;

import static org.junit.Assert.assertTrue;
import static org.lwjgl.glfw.GLFW.*;

public class ModelTest extends App {

    private static Engine ENGINE = new Engine();
    private Graphics g;
    private Model model;
    private Camera3D cam;
    private Light light;
    private Shaders normal, edge;
    private Mesh light_cube;
    private RenderTarget rt;
    private TextureRegion inv_uv;
    private Mesh skybox;


    {
        TITLE = "ModelTest";
        WIDTH = 800;
        HEIGHT = 600;
        MAXIMISED = false;
        //hints.put(GLFW_SAMPLES, 4);
    }

    @Override
    public void dispose() {

    }

    float rotation;
    @Override
    public void render() {
        glClearColor(1,1,1,1);
        glEnable(GL_CULL_FACE);
        g.begin(rt, true);
        {
            g.setCamera(cam);
            g.pushShader(ResourceManager.INSTANCE.getShader("/Shaders/default.vs", "/Shaders/default.fs", new Pair<>("BLINN", "0"), new Pair<>("TEXTURED", "0"), new Pair<>("SPEC_MAP", "0")));
            {
                Light.lightbuffer.bindToShader(g.getCurrentShader());
                Light.lightbuffer.put(0, light.toArray());
                Light.lightbuffer.put(1, 0.9f);
                model.draw(g, 0, 0, 0, new Vector3f(1), rotation, new Vector3f(1,1,1), Color.WHITE);
            }
            g.popShader();
            g.pushShader(ResourceManager.INSTANCE.getShader("/Shaders/default.vs", "/Shaders/default.fs", new Pair<>("UNSHADED", "0")));
            {
                light_cube.draw(g, light.pos.x, light.pos.y, light.pos.z, new Vector3f(1), 0, new Vector3f(0,0,0), Color.RED);
                skybox.draw(g, 0, 0, 0, new Vector3f(900), 0, new Vector3f(0,0,0), Color.GREEN);
                
            }
            g.popShader();
        }
        g.end();
        g.begin(null, true);
        {
            g.pushShader(ResourceManager.INSTANCE.getShader("/Shaders/default.vs", "/Shaders/default.fs", new Pair<>("HDR", "0"), new Pair<>("SRGB", "0")));
            g.drawTexture(rt.texture[0], inv_uv, 0, 0, 0, WIDTH, HEIGHT, 0, 0, 0, Color.WHITE);
            g.popShader();
        }
        g.end();
    }

    float yaw, pitch;
    boolean togglez, togglex;
    double deltatime;
    @Override
    public void update() {
        rotation++;
        Vector2d delta = InputHandler.INSTANCE.getMouseDelta();
        yaw += delta.x;
        pitch += delta.y;
        if(pitch > 89.0f)
            pitch =  89.0f;
        if(pitch < -89.0f)
            pitch = -89.0f;
        cam.rotate(yaw, -pitch);

        if(InputHandler.INSTANCE.isKeyPressed(GLFW_KEY_W)){
            cam.moveForward(0.25f);
        }
        else if(InputHandler.INSTANCE.isKeyPressed(GLFW_KEY_S)){
            cam.moveBackward(0.25f);
        }
        else if(InputHandler.INSTANCE.isKeyPressed(GLFW_KEY_A)){
            cam.strafeLeft(0.25f);
        }
        else if(InputHandler.INSTANCE.isKeyPressed(GLFW_KEY_D)){
            cam.strafeRight(0.25f);
        }
        //rotation++;
        if(deltatime > 30.0){
            ENGINE.end();
        }
        deltatime += Timer.getDelta();
    }

    @Override
    public void init() {
        g = ENGINE.createGraphics(this);
        vp = new Viewport(0, 0, WIDTH, HEIGHT);
        vp.apply();
        model = new MeshLoader("C:/Users/B/Documents/DabEngine/src/test/resources/PC Computer - Sonic Generations - Sonic the Hedgehog Modern/Sonic (Modern)/Sonic.DAE.dae").toModel();
        cam = new Camera3D(45, WIDTH, HEIGHT);
        light = new Light(new Vector3f(0, -25, 40), new Vector3f(1360));
        glfwSetInputMode(ENGINE.getMainWindow().getWin(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);  
        
        light_cube = new Cube().generate(0,0,0).toMesh();

        rt = new RenderTarget(WIDTH, HEIGHT, vp, new Texture(null, WIDTH, HEIGHT, true, Parameters.LINEAR));

        inv_uv = new TextureRegion();
        inv_uv.uv = new Vector4f(0,1,1,0);

        skybox = new Cube().generate(0, 0, 0).toMesh();

    }

    @Override
    public void resize(int width, int height) {
        vp.update(width, height);
        vp.apply();
    }

    public static void main(String[] args){
        ENGINE.init(new ModelTest());
        ENGINE.run();
    }

    @Test
    public void testMain(){
        try {
            ModelTest.main(null);
        } catch(Exception e){
            System.out.println("No drawing available");
        }
    }

}
