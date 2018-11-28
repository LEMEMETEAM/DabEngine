import Graphics.Models.*;
import Graphics.Shaders;

public class Brick extends Model {

    private Texture texture;
    private Shaders s;

    public Brick(Shaders s){
        super(new Mesh(
                new float[]{
                        -1.5f, 0.25f, 0f,
                        -1.5f, -0.25f, 0f,
                        1.5f, -0.25f, 0f,
                        1.5f, 0.25f, 0f
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

    }


    @Override
    public void render() {

    }

    @Override
    public void update() {

    }
}
