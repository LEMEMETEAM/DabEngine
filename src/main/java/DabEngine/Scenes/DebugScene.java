package DabEngine.Scenes;

import org.joml.Vector2f;
import org.joml.Vector3f;

import DabEngine.Core.App;
import DabEngine.Core.Engine;
import DabEngine.Entities.Entity;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CText;
import DabEngine.Entities.Components.CTransform;
import DabEngine.Entities.Components.Component;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.Batch.Font;
import DabEngine.Graphics.OpenGL.Viewport.Viewport;
import DabEngine.System.ComponentSystem;
import DabEngine.System.RendererSystem;
import DabEngine.Utils.Color;
import DabEngine.Utils.Timer;

public class DebugScene extends Scene {
    
    public Engine engine;
    private Font f;

    public DebugScene(App app, Engine e, Font f){
        super(app, false);
        this.engine = e;
        this.f = f;
    }

    @Override
    public void init() {
        // fps counter
        CTransform tr = new CTransform();
        tr.pos = new Vector3f(0, f.size, 0);

        CText t = new CText();
        t.font = f;
        t.color = Color.WHITE;
        t.text = "";
        t.textShader = Font.TEXT_DEFAULT_SHADER;
        
        Entity text = EntityManager.createEntity(tr, t, new CDebug());

        addSystem(new DebugSystem(engine));
        addSystem(new RendererSystem());
    }

}

class CDebug extends Component {
}

class DebugSystem extends ComponentSystem {

    private Engine engine;

    public DebugSystem(Engine engine){
        this.engine = engine;
    }

    @Override
    public void update() {
        for(Entity e  : EntityManager.entitiesWithComponents(CDebug.class)){
            if(e.hasComponent(CText.class)){
                e.getComponent(CText.class).text = "UPS: " + engine.UPDATES + ", FPS: " + engine.FRAMES + ",  LATENCY: " + Timer.getDelta();
            }
        }
    }

    @Override
    public void render(Graphics g) {

    }
    
}