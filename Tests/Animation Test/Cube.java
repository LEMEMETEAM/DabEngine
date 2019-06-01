import Graphics.Models.Mesh;
import Graphics.Models.Model;
import Graphics.Models.Texture;
import Graphics.Shaders;

public class Cube extends Model {

    private Texture texture;
    private Shaders s;

    public Cube(Shaders s) {
        super(new Mesh(
                new float[]{
                        -0.5f, 0.5f, 0f, //FRONT TOP LEFT
                        -0.5f, -0.5f, 0f, //FRONT BOTTOM LEFT
                        0.5f, -0.5f, 0f, //FRONT BOTTOM RIGHT
                        0.5f, 0.5f, 0f, // FRONT TOP RIGHT

                        -0.5f, 0.5f, -0.5f, //BACK TOP LEFT
                        -0.5f, -0.5f, -0.5f, //BACK BOTTOM LEFT
                        0.5f, -0.5f, -0.5f, //BACK BOTTOM RIGHT
                        0.5f, 0.5f, -0.5f, // BACK TOP RIGHT
                },
                new float[]{
                        0, 0,
                        0, 1,
                        1, 1,
                        1, 0,

                        0, 0,
                        0, 1,
                        1, 1,
                        1, 0,

                        0, 0,
                        0, 1,
                        1, 1,
                        1, 0,

                        0, 0,
                        0, 1,
                        1, 1,
                        1, 0,

                        0, 0,
                        0, 1,
                        1, 1,
                        1, 0,

                        0, 0,
                        0, 1,
                        1, 1,
                        1, 0
                },
                new int[]{
                        0, 1, 2,// FRONT
                        0, 3, 2,

                        4, 0, 3,// TOP
                        4, 7, 3,

                        4, 5, 6,//BACK
                        4, 7, 6,

                        5, 1, 2,//BOTTOM
                        5, 6, 2,

                        4, 5, 1,//LEFT
                        4, 0, 1,

                        7, 6, 2,//RIGHT
                        7, 3, 2
                }
        ));

        this.s = s;
        texture = new Texture("./res/weighted_companion_cube_by_befree2209-d9u3xqm.png");

    }

    @Override
    public void render() {
        texture.bind();
        s.setUniform("modelMatrix", getProjection());
        getMesh().render();
    }

    @Override
    public void update() {

    }
}
