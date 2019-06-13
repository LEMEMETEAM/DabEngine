package DabEngine.Scenes;

import DabEngine.Core.App;
import DabEngine.Core.Engine;
import DabEngine.Entities.Entity;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CText;
import DabEngine.Entities.Components.Component;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.Batch.Font;
import DabEngine.System.ComponentSystem;
import DabEngine.System.RendererSystem;
import DabEngine.Utils.Colors;

public class DebugScene extends Scene {
    
    public Engine engine;

    public DebugScene(Engine e){
        this.engine = e;
    }

    @Override
    public void init() {
        // fps counter
        CText t = new CText();
        t.font = new Font("src/main/resources/Fonts/OpenSans-Light.ttf", 8, 3);
        t.color = Colors.WHITE.color;
        t.text = "";
        Entity text = EntityManager.createEntity(t, new CDebug());

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
                e.getComponent(CText.class).text = "UPS: " + engine.UPDATES + ", FPS: " + engine.FRAMES;
            }
        }
    }

    @Override
    public void render(Graphics g) {

    }
    
}