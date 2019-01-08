import Core.Engine;
import Graphics.Models.Mesh;
import Graphics.Models.Model;
import Graphics.Models.Texture;
import Graphics.Models.TextureSheet;
import Graphics.Shaders;
import Input.*;
public class Sprite extends Model {

    private TextureSheet texture;
    private Shaders s;
    private int tex_x, tex_y;
    private double lasttime = 0;
    private double elapsedtime = 0;

    public Sprite(Shaders s){
        super(new Mesh(
                new float[]{
                        -4f, 4f, 0f,
                        -4f, -4f, 0f,
                        4f, -4f, 0f,
                        4f, 4f, 0f
                },
                new float[]{
                        0, 0,
                        0, 1,
                        1, 1,
                        1, 0
                },
                new int[]{
                        0, 1, 2,
                        0, 3, 2
                }
        ));

        this.s = s;
        texture = new TextureSheet("./res/c7f297523ce57fc.png", 6);
    }
    @Override
    public void render() {
        texture.bindTex(s, tex_x, tex_y, 1, 2);
        s.setUniform("modelMatrix", getProjection());
        getMesh().render();
    }

    @Override
    public void update() {

        elapsedtime += Engine.getDelta() - lasttime;

        if(elapsedtime >= 1f/60f){
            elapsedtime = 0;
            if(tex_x > 8){
                tex_x = 0;
            }
            tex_x++;
        }
        lasttime = Engine.getDelta();
    }
}
