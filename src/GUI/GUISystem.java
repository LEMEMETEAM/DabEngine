package GUI;

import java.lang.ref.WeakReference;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2fc;

import Entities.GameObject;
import Entities.Components.CRender;
import Entities.Components.CTransform;
import Graphics.Batch.SpriteBatch;
import Input.InputHandler;
import System.RenderSystem;
import System.System;

public class GUISystem extends System {

	@Override
	public void update() {
		// TODO Auto-generated method stub
		for(WeakReference<GameObject> gui : obj) {
			if(gui.get() instanceof GUIObject) {
				CTransform trans = gui.get().getComponent(CTransform.class);
				Vector2f half_extent = new Vector2f(trans.size.x/2, trans.size.y/2);
				Vector2f mouse_pos = new Vector2f((float)InputHandler.INSTANCE.getMousePos().x, (float) InputHandler.INSTANCE.getMousePos().y);
				Vector2f distance = mouse_pos.sub(trans.pos, new Vector2f());
				
				distance.x = (float) Math.abs(distance.x);
				distance.y = (float) Math.abs(distance.y);
				
				distance.sub(half_extent.add(new Vector2f(1, 1), new Vector2f()));
				
				GUIObject guiobj = (GUIObject) gui.get();
				if(distance.x < 0 && distance.y < 0) {
					guiobj.onHover();
				}
				else {
					guiobj.onExit();
				}
			}
		}
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		for(WeakReference<GameObject> gui : obj) {
			if(gui.get() instanceof GUIObject) {
				CRender render = gui.get().getComponent(CRender.class);
				CTransform trans = gui.get().getComponent(CTransform.class);
				SpriteBatch batch = this.state.get().getSystem(RenderSystem.class).getBatch();
				
				batch.begin();
				batch.draw(render.texture, trans.pos.x, trans.pos.y, trans.size.x, trans.size.y, render.color.x, render.color.y, render.color.z, render.color.w, render.center_anchor);
				batch.end();
			}
		}
	}

}
