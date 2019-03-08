package GUI;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.newdawn.slick.Color;

import Entities.Entity;
import Entities.Components.CRender;
import Entities.Components.CTransform;
import GUI.Components.CText;
import Graphics.Batch.SpriteBatch;
import Graphics.Batch.TextBatch;
import Input.InputHandler;
import System.RenderSystem;
import System.System;

public abstract class AbstractMenu {
	
	private SpriteBatch batch = new SpriteBatch();
	public ArrayList<GUIObject> obj = new ArrayList<>();

	public void update() {
		// TODO Auto-generated method stub
		for(GUIObject gui : obj) {
			Vector2f half_extent = new Vector2f(gui.size.x/2, gui.size.y/2);
			Vector2f mouse_pos = new Vector2f((float)InputHandler.INSTANCE.getMousePos().x, (float) InputHandler.INSTANCE.getMousePos().y);
			Vector2f distance = mouse_pos.sub(gui.pos, new Vector2f());
			
			distance.x = (float) Math.abs(distance.x);
			distance.y = (float) Math.abs(distance.y);
			
			distance.sub(half_extent.add(new Vector2f(1, 1), new Vector2f()));
			
			if(distance.x < 0 && distance.y < 0) {
				gui.onHover();
			}
			else {
				gui.onExit();
			}
		}
	}

	public void render() {
		// TODO Auto-generated method stub
		for(GUIObject gui : obj) {
			if(gui.tex != null) {
				batch.begin();
				batch.draw(gui.tex, gui.pos.x, gui.pos.y, gui.size.x, gui.size.y, gui.color.x, gui.color.y, gui.color.z, gui.color.w, true);
				batch.end();
			}
		}
	}
}
