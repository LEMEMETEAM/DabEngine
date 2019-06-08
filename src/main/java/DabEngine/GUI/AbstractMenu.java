package DabEngine.GUI;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glScissor;

import java.util.ArrayList;

import org.joml.Vector2f;

import DabEngine.GUI.Objects.Button;
import DabEngine.GUI.Objects.Image;
import DabEngine.GUI.Objects.Panel;
import DabEngine.Graphics.Graphics;
import DabEngine.Graphics.Batch.Font;
import DabEngine.Input.InputHandler;

public abstract class AbstractMenu {
	
	public ArrayList<Panel> obj = new ArrayList<>();
	

	public void update() {
		// TODO Auto-generated method stub
		var objs = unpackPanels();
		for(var g : objs){
			checkHover(g);
		}
	}

	private ArrayList<GUIObject> unpackPanels(){
		ArrayList<GUIObject> objs = new ArrayList<>();
		for(int i = 0; i < obj.size(); i++){
			Panel p = obj.get(i);
			for(var pan : p.panel_objects){
				if(pan instanceof Panel){
					p = (Panel)pan;
				}
				else{
					objs.add(pan);
				}
			}
		}
		int last_val;
		for(var gui : objs){
			for(var gui_2 : objs){

			}
		}
		return objs;
	}

	public void render(Graphics g) {
		// TODO Auto-generated method stub
		var objs = unpackPanels();
		g.begin();
		for(var o : objs){
			o.render(g);
		}
		g.end();
	}
	
	private void checkHover(GUIObject g) {
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
