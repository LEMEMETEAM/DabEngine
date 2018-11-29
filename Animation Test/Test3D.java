import Core.Engine;
import Graphics.Camera3D;
import Graphics.HUD;
import Graphics.Shaders;
import Stage.Stage;
import Input.*;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Test3D extends Engine {

    private HUD gui;
    private Camera3D cam;
    private Shaders s;
    private Stage stage;

    @Override
    public void initWindow(int width, int height, String title) {
        super.initWindow(1366, 768, "TEst3d");

        glfwSetInputMode(getMainWindow().getWin(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        setProjection("3D");

        cam = new Camera3D();

        gui = new HUD(getMainWindow().getWidth(), getMainWindow().getHeight());

        stage = new Stage();

        s = new Shaders("shaders");
        s.createUniform("projectionMatrix");
        s.createUniform("viewMatrix");
        s.createUniform("modelMatrix");
        s.createUniform("texture_sampler");

        stage.addToStage(new Cube(s));
    }

    @Override
    public void onRender() {
        gui.drawText(Integer.toString(getFrames()) + " FPS", 5, 20, "BOLD", "./res/OpenSans-Bold.ttf");
        gui.drawText(Integer.toString(getFrames()) + " UPS", 5, 40, "BOLD", "./res/OpenSans-Bold.ttf");
        gui.drawText(Float.toString(getFOV()) + " FOV", 5, 60, "BOLD", "./res/OpenSans-Bold.ttf");
        gui.drawText("Yaw: " + cam.getYaw() + " - Pitch:" + cam.getPitch(), 50, 20, "BOLD", "./res/OpenSans-Bold.ttf");
        gui.drawText("Mouse stuff: " + Mouse.dx + " - " + Mouse.dy, 50, 40, "BOLD", "./res/OpenSans-Bold.ttf");

        s.bind();

        s.setUniform("projectionMatrix", getProjectionMatrix());
        s.setUniform("viewMatrix", cam.getProjection());
        s.setUniform("texture_sampler", 0);

        stage.render();
    }

    @Override
    public void onUpdate() {
        if(Mouse.isMoving()) {
            cam.addYaw((float) Mouse.dx * 0.05f);
            cam.addPitch((float) Mouse.dy * 0.05f);
            Mouse.setMoving(false);
        }

        if(Input.keys[GLFW_KEY_A]){
            cam.addPosition(new Vector3f(0.1f, 0f, 0f));
        }
        if(Input.keys[GLFW_KEY_D]){
            cam.addPosition(new Vector3f(-0.1f, 0f, 0f));
        }
        if(Input.keys[GLFW_KEY_W]){
            cam.addPosition(new Vector3f(0f, 0f, 0.1f));
        }
        if(Input.keys[GLFW_KEY_S]){
            cam.addPosition(new Vector3f(0f, 0f, -0.1f));
        }
    }

    public static void main(String[] args) {
        new Test3D().start();
    }
}
