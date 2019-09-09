package DabEngine.System;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.joml.Vector3f;

import DabEngine.Entities.EntityFilter;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CCamera;
import DabEngine.Entities.Components.CLight;
import DabEngine.Entities.Components.CModel;
import DabEngine.Entities.Components.CRenderable;
import DabEngine.Entities.Components.CShader;
import DabEngine.Entities.Components.CSprite;
import DabEngine.Entities.Components.CText;
import DabEngine.Entities.Components.CTileMap;
import DabEngine.Entities.Components.CTransform;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.OpenGL.RenderTarget;
import DabEngine.Graphics.OpenGL.Light.Light;
import DabEngine.Graphics.OpenGL.Viewport.Viewport;
import DabEngine.Utils.Color;
import DabEngine.Utils.FixedArrayList;

public class RendererSystem extends ComponentSystem {

    private ArrayList<Integer> depth = new ArrayList<>();
    private int camera;
    private FixedArrayList<Integer> lights = new FixedArrayList<>(32);

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        EntityManager.INSTANCE.each(e -> {
            if(EntityManager.INSTANCE.has(e, CCamera.class)){
                camera = e;
            }
            else if(EntityManager.INSTANCE.has(e, CLight.class)){
                lights.add(e);
            }
            else{
                depth.add(e);
            }
        }, EntityFilter.has(CTransform.class).and(CRenderable.class).or(CCamera.class).or(CLight.class));

        depth.sort((c1, c2) -> Float.compare((EntityManager.INSTANCE.component(c2, CTransform.class).pos.z), (EntityManager.INSTANCE.component(c2, CTransform.class).pos.z)));

        g.begin(scene.config.renderToTexture ? scene.rt : null, true);
        if(camera != 0)
            g.setCamera(EntityManager.INSTANCE.component(camera, CCamera.class).camera);
        for(int l : lights){
            CLight light = EntityManager.INSTANCE.component(l, CLight.class);
            Light.lightbuffer.bindToShader(g.getCurrentShader());
            Light.lightbuffer.put(0, light.light.toArray());
            Light.lightbuffer.put(1, scene.ambientStrength);
        }
        for(int e : depth){
            CTransform t = EntityManager.INSTANCE.component(e, CTransform.class);
            CRenderable r = EntityManager.INSTANCE.component(e, CRenderable.class);
            CShader s = EntityManager.INSTANCE.component(e, CShader.class);
            
            if(s!=null){
                g.pushShader(s.shader);
                Light.lightbuffer.bindToShader(g.getCurrentShader());
            }
            if(EntityManager.INSTANCE.has(e, CModel.class)){
                CModel m = EntityManager.INSTANCE.component(e, CModel.class);
                m.model.draw(g, t.pos.x, t.pos.y, t.pos.z, t.size, t.rotation.w, new Vector3f(t.rotation.x, t.rotation.y, t.rotation.z), r.color);
            }
            if(s!=null){
                g.popShader();
            }
        }
        g.end();
        if(scene.config.renderToTexture){
            g.begin(null, true);
            {
                g.drawTexture(scene.rt.texture[0], scene.inv_uv, 0, 0, 0, scene.app.WIDTH, scene.app.HEIGHT, 0, 0, 0, Color.WHITE);
            }
            g.end();
        }
        depth.clear();
        lights.clear();
        camera = 0;
    }

    
}