import Core.Engine;
import Graphics.Camera2D;
import Graphics.Shaders;
import Stage.Stage;

import static org.lwjgl.opengl.GL11.glClearColor;

public class Anim2D extends Engine {

    private Shaders shaders;
    private Stage stage;
    private Camera2D cam;

    public void initWindow(int width, int height, String title) {
        width = 1366;
        height = 768;
        title = "dab";

        super.initWindow(width, height, title);

        glClearColor(0.5f, 0.75f, 0.75f, 1f);

        setProjection("2D");

        shaders = new Shaders("shaders");
        shaders.createUniform("projectionMatrix");
        shaders.createUniform("viewMatrix");
        shaders.createUniform("modelMatrix");
        shaders.createUniform("texture_sampler");
        shaders.createUniform("texModifier");

        stage = new Stage();
        cam = new Camera2D();

        stage.addToStage(new Sprite(shaders));
    }

    @Override
    public void onRender() {
        shaders.bind();

        shaders.setUniform("projectionMatrix", getProjectionMatrix());
        shaders.setUniform("viewMatrix", cam.getProjection());
        shaders.setUniform("texture_sampler", 0);

        stage.render();
    }

    @Override
    public void onUpdate() {
        stage.update();
    }

    public static void main(String[] args) {
        new Anim2D().start();
    }
}
