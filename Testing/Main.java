import Core.Engine;
import Graphics.Camera3D;
import Graphics.Shaders;
import Stage.Stage;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class Main extends Engine {

    private Shaders shaders;
    private Stage stage;
    private Camera3D cam;
    private Ball ball;
    private Paddle paddle;
    private Menu menu;

    @Override
    public void initWindow() {
        super.initWindow();

        glClearColor(0.1f, 0.5f, 0.5f, 1f);

        stage = new Stage();

        cam = new Camera3D();

        shaders = new Shaders("shaders");
        shaders.createUniform("projectionMatrix");
        shaders.createUniform("viewMatrix");
        shaders.createUniform("modelMatrix");
        shaders.createUniform("texture_sampler");

        paddle = new Paddle(shaders);
        ball = new Ball(shaders);

        stage.addToStage(paddle);
        stage.addToStage(ball);

        glViewport(0, 0, getMainWindow().getWidth(), getMainWindow().getHeight());

        cam.addPosition(new Vector3f(0, 0, -0.5f));

        menu = new Menu();

    }

    @Override
    public void onRender() {
        shaders.bind();

        shaders.setUniform("projectionMatrix", getProjectionMatrix());
        shaders.setUniform("viewMatrix", cam.getProjection());
        shaders.setUniform("texture_sampler", 0);

        stage.render();

        menu.render();
    }

    @Override
    public void onUpdate() {
        stage.update();
        if(ball.collision(paddle.getBounds())){
            ball.setVelocityx(-1f * ball.getDEFAULT_VEL());
            ball.setVelocityy(-1f * ball.getDEFAULT_VEL());

            ball.setVelocityx(-0f * ball.getVelocityx());
            ball.setVelocityy(-1f * ball.getVelocityy());
            System.out.println("Collision with bound 1");
        }
        if(ball.collision(paddle.getBounds2())){
            ball.setVelocityx(-1f * ball.getDEFAULT_VEL());
            ball.setVelocityy(-1f * ball.getDEFAULT_VEL());

            ball.setVelocityx(-0.5f * ball.getVelocityx());
            ball.setVelocityy(-1f * ball.getVelocityy());
            System.out.println("Collision with bound 2");
        }
        else if(ball.collision(paddle.getBounds3())){
            ball.setVelocityx(-1f * ball.getDEFAULT_VEL());
            ball.setVelocityy(-1f * ball.getDEFAULT_VEL());

            ball.setVelocityx(-1f * ball.getVelocityx());
            ball.setVelocityy(-1f * ball.getVelocityy());
            System.out.println("Collision with bound 3");
        }
        else if(ball.collision(paddle.getBounds4())){
            ball.setVelocityx(-1f * ball.getDEFAULT_VEL());
            ball.setVelocityy(-1f * ball.getDEFAULT_VEL());

            ball.setVelocityx(-1f * ball.getVelocityx());
            ball.setVelocityy(-0.5f * ball.getVelocityy());
            System.out.println("Collision with bound 4");
        }
    }

    public static void main(String[] args) {
        new Test3D().start();
    }
}
