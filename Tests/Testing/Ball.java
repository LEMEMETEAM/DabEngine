
import Graphics.Models.Mesh;
import Graphics.Models.Model;
import Graphics.Models.Texture;
import Graphics.Shaders;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Ball extends Model {

    private Shaders s;
    private final float DEFAULT_VEL = 0.2f;
    private float velocityx = 0.2f;
    private float velocityy = 0.2f;
    private Texture texture;

    public Ball(Shaders s){
        super(new Mesh(
                new float[]{
                        -0.25f, 0.25f, 0f,
                        -0.25f, -0.25f, 0f,
                        0.25f, -0.25f, 0f,
                        0.25f, 0.25f, 0f
                },
                new float[]{
                        0f, 0f,
                        0f, 0.5f,
                        0.5f, 0.5f,
                        0.5f, 0f

                },
                new int[]{
                        0, 1, 2,
                        0, 3, 2
                }
        ));
        this.s = s;

        texture = new Texture("./res/spritesheet.png");

        getBounds().setHalf_extent(new Vector2f(0.25f, 0.25f));
    }

    public void render(){
        texture.bind();
        s.setUniform("modelMatrix", getProjection());
        getMesh().render();
    }

    @Override
    public void update() {
        addPosition(new Vector3f(velocityx, velocityy, 0));

        if(getPosition().x >= 10 || getPosition().x <= -10){
            velocityx *= -1;
        }
        if(getPosition().y >= 10 || getPosition().y <= -10){
            velocityy *= -1;
        }
    }

    public float getVelocityx() {
        return velocityx;
    }

    public void setVelocityx(float velocity) {
        this.velocityx = velocity;
    }

    public float getVelocityy() {
        return velocityy;
    }

    public void setVelocityy(float velocityy) {
        this.velocityy = velocityy;
    }

    public float getDEFAULT_VEL() {
        return DEFAULT_VEL;
    }
}
