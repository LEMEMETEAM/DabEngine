package DabEngine.Scenes;

import org.joml.Vector2f;
import org.joml.Vector3f;

import DabEngine.Core.App;
import DabEngine.Core.Engine;
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

public class DebugOverlay extends Overlay {
    
    public Engine engine;
    private Font f;
    private String text;

    public DebugOverlay(Engine e, Font f){
        this.engine = e;
        this.f = f;
    }

    @Override
    public void update() {
        text = "UPS: " + engine.UPDATES + ", FPS: " + engine.FRAMES + ",  LATENCY: " + Timer.getDelta();
    }

    @Override
    public void render(Graphics g) {
        g.begin(null);
        g.pushShader(Font.TEXT_DEFAULT_SHADER);
        g.drawText(f, text, 0, 0, 0, Color.GREEN);
        g.popShader();
        g.end();
    }
}