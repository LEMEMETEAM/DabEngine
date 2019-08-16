package DabEngine;

import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector3f;

import DabEngine.Core.App;
import DabEngine.Core.DabEngineConfig;
import DabEngine.Core.Engine;
import DabEngine.Graphics.Camera3D;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.Models.MeshLoader;
import DabEngine.Graphics.Models.Model;
import DabEngine.Graphics.OpenGL.Light.Light;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.OpenGL.Viewport.Viewport;
import DabEngine.Input.InputHandler;
import DabEngine.Utils.Pair;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.glfw.GLFW.*;

public class ModelTest extends App {

    private static Engine ENGINE = new Engine();
    private Graphics g;
    private Model model;
    private Camera3D cam;
    private Light light;


    {
        TITLE = "ModelTest";
        WIDTH = 800;
        HEIGHT = 600;
        MAXIMISED = false;
        hints.put(GLFW_SAMPLES, 4);
    }

    @Override
    public void dispose() {

    }

    float rotation;
    @Override
    public void render() {
        glClearColor(1,1,1,1);
        g.begin(null, false);
            g.setCamera(cam);
            g.pushShader(Shaders.getUberShader("/Shaders/default.vs", "/Shaders/default.fs", new Pair<>("LIT", "null"), new Pair<>("TEXTURED", "0")));
            {
                Light.lightbuffer.bindToShader(g.getCurrentShader());
                Light.lightbuffer.put(0, light.toArray());
                Light.lightbuffer.put(1, 0.1f);
                model.draw(g, 0, 0, 0, 1, 0, new Vector3f(1,0,0));
            }
            g.popShader();
        g.end(false);
    }

    float yaw, pitch;
    boolean togglez, togglex;
    @Override
    public void update() {
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
        if(InputHandler.INSTANCE.isKeyPressed(GLFW_KEY_Z)){
            togglez = togglez ? false : true;
            DabEngineConfig.MULTISAMPLE.setValue(Boolean.toString(togglez));
        }
        if(InputHandler.INSTANCE.isKeyPressed(GLFW_KEY_X)){
            togglex = togglex ? false : true;
            DabEngineConfig.VSYNC.setValue(Boolean.toString(togglex));
        }
    }

    @Override
    public void init() {
        g = ENGINE.createGraphics(this);
        vp = new Viewport(0, 0, WIDTH, HEIGHT);
        vp.apply();
        model = new MeshLoader("C:/Users/B/Documents/DabEngine/src/test/resources/Nintendo 64 - Super Mario 64 - Peachs Castle Exterior/princess peaches castle (outside).obj").toModel();
        cam = new Camera3D(45, WIDTH, HEIGHT);
        light = new Light(new Vector3f(0, 5, 0), new Vector3f(1, 1, 1));
        glfwSetInputMode(ENGINE.getMainWindow().getWin(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);  
        
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

}