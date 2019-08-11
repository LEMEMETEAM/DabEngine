package DabEngine;

import org.joml.Vector3f;

import DabEngine.Core.App;
import DabEngine.Core.Engine;
import DabEngine.Graphics.Camera3D;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.Models.MeshLoader;
import DabEngine.Graphics.Models.Model;
import DabEngine.Graphics.OpenGL.Light.Light;
import DabEngine.Graphics.OpenGL.Viewport.Viewport;

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
    }

    @Override
    public void dispose() {

    }

    @Override
    public void render() {
        g.begin(null);
            g.setCamera(cam);
            g.pushShader(Light.LIGHT_SHADER);
            {
                Light.lightbuffer.bindToShader(g.getCurrentShader());
                Light.lightbuffer.put(0, light.toArray());
                g.drawModel(model, 0, 0, 0, 0.01f);
            }
        g.end();
    }

    @Override
    public void update() {
        //cam.rotate(0.01, 0);
    }

    @Override
    public void init() {
        g = ENGINE.createGraphics(this);
        vp = new Viewport(0, 0, WIDTH, HEIGHT);
        vp.apply();
        model = new MeshLoader("C:/Users/B/Documents/DabEngine/src/test/resources/PC Computer - Quake - Ranger/player/player.obj").toModel();
        cam = new Camera3D(45, WIDTH, HEIGHT);
        light = new Light(new Vector3f(0, 30, 0), new Vector3f(1, 1, 1));
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