package GUI;

import java.util.ArrayList;

import org.joml.Vector2f;

import GUI.Objects.Panel;
import Graphics.Batch.SpriteBatch;
import Input.InputHandler;

import static org.lwjgl.opengl.GL11.*;

public abstract class AbstractMenu {
	
	public ArrayList<Panel> obj = new ArrayList<>();

	public void update() {
		// TODO Auto-generated method stub
		for(var panel : obj) {
			Vector2f half_extent = new Vector2f(panel.size.x/2, panel.size.y/2);
			Vector2f mouse_pos = new Vector2f((float)InputHandler.INSTANCE.getMousePos().x, (float) InputHandler.INSTANCE.getMousePos().y);
			Vector2f distance = mouse_pos.sub(panel.pos, new Vector2f());
			
			distance.x = (float) Math.abs(distance.x);
			distance.y = (float) Math.abs(distance.y);
			
			distance.sub(half_extent.add(new Vector2f(1, 1), new Vector2f()));
			
			if(distance.x < 0 && distance.y < 0) {
				panel.onHover();
			}
			else {
				panel.onExit();
			}
			for(GUIObject g : panel.panel_objects) {
				Vector2f half_extent_g = new Vector2f(g.size.x/2, g.size.y/2);
				Vector2f mouse_pos_g = new Vector2f((float)InputHandler.INSTANCE.getMousePos().x, (float) InputHandler.INSTANCE.getMousePos().y);
				Vector2f distance_g = mouse_pos_g.sub(g.pos, new Vector2f());
				
				distance_g.x = (float) Math.abs(distance_g.x);
				distance_g.y = (float) Math.abs(distance_g.y);
				
				distance_g.sub(half_extent_g.add(new Vector2f(1, 1), new Vector2f()));
				
				if(distance_g.x < 0 && distance_g.y < 0) {
					g.onHover();
				}
				else {
					g.onExit();
				}
			}
		}
	}

	public void render(Graphics g) {
		// TODO Auto-generated method stub
		batch.begin();
		for(var gui : obj) {
			if(gui.tex != null) {
				batch.draw(gui.tex, gui.pos.x, gui.pos.y, gui.size.x, gui.size.y, gui.color.x, gui.color.y, gui.color.z, gui.color.w, true);
			}
		}
		batch.end();
	}
}
