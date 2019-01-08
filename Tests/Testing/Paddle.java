import Graphics.Models.AABB;
import Graphics.Models.Mesh;
import Graphics.Models.Model;
import Graphics.Models.Texture;
import Graphics.Shaders;
import Input.Input;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

public class Paddle extends Model {

    private Shaders s;
    private float velocity = 0.5f;
    private Texture texture;
    private AABB bounds2;
    private AABB bounds3;
    private AABB bounds4;

    public Paddle(Shaders s){
        super(new Mesh(
                new float[]{
                        -2f, 0.25f, 0f,
                        -2f, -0.25f, 0f,
                        2f, -0.25f, 0f,
                        2f, 0.25f, 0f
                },
                new float[]{
                   0f, 0.5f,
                   0f, 1f,
                   1f, 1f,
                   1f, 0.5f
                },
                new int[]{
                        0, 1, 2,
                        0, 3, 2
                }
        ));
        this.s = s;

        texture = new Texture("./res/spritesheet.png");

        setPosition(new Vector3f(0, -(0.75f*10f), 0f));

        getBounds().setHalf_extent(new Vector2f(0.5f, 0.25f));

        bounds2 = new AABB(new Vector2f(getPosition().x, getPosition().y), new Vector2f(1f, 0.25f));
        bounds3 = new AABB(new Vector2f(getPosition().x, getPosition().y), new Vector2f(1.5f, 0.25f));
        bounds4 = new AABB(new Vector2f(getPosition().x, getPosition().y), new Vector2f(2f, 0.25f));
    }

    public void render(){
        texture.bind();
        s.setUniform("modelMatrix", getProjection());
        getMesh().render();
    }

    @Override
    public void update() {
        if(getPosition().x >= 10){
            setPosition(new Vector3f(10, getPosition().y, getPosition().z));
            bounds2.setCenter(new Vector2f(10, getPosition().y));
            bounds3.setCenter(new Vector2f(10, getPosition().y));
            bounds4.setCenter(new Vector2f(10, getPosition().y));
        }
        else if(getPosition().x <= -10){
            setPosition(new Vector3f(-10, getPosition().y, getPosition().z));
            bounds2.setCenter(new Vector2f(-10, getPosition().y));
            bounds3.setCenter(new Vector2f(-10, getPosition().y));
            bounds4.setCenter(new Vector2f(-10, getPosition().y));
        }
        if(Input.keys[GLFW_KEY_A]){
            addPosition(new Vector3f(-velocity, 0, 0));
            bounds2.addToCenter(new Vector2f(-velocity, 0));
            bounds3.addToCenter(new Vector2f(-velocity, 0));
            bounds4.addToCenter(new Vector2f(-velocity, 0));
        }
        else if(Input.keys[GLFW_KEY_D]){
            addPosition(new Vector3f(velocity, 0, 0));
            bounds2.addToCenter(new Vector2f(velocity, 0));
            bounds3.addToCenter(new Vector2f(velocity, 0));
            bounds4.addToCenter(new Vector2f(velocity, 0));
        }
    }

    public AABB getBounds2() {
        return bounds2;
    }

    public AABB getBounds3() {
        return bounds3;
    }

    public AABB getBounds4() {
        return bounds4;
    }
}
