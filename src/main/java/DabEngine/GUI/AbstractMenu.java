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
import DabEngine.Graphics.Batch.PolygonBatch;
import DabEngine.Graphics.Batch.SpriteBatch;
import DabEngine.Graphics.Batch.TextBatch;
import DabEngine.Input.InputHandler;

public abstract class AbstractMenu {
	
	public ArrayList<Panel> obj = new ArrayList<>();
	public Font font;

	public void update() {
		// TODO Auto-generated method stub
		for(var panel : obj) {
			checkHover(panel);
			for(GUIObject g : panel.panel_objects) {
				if(g instanceof Panel) {
					for(GUIObject g2 : ((Panel) g).panel_objects) {
						checkHover(g2);
					}
				}
				else {
					checkHover(g);
				}
			}
		}
	}

	public void render(Graphics g) {
		// TODO Auto-generated method stub
		for(var panel : obj) {
			glScissor((int)panel.pos.x, (int)panel.pos.y, (int)panel.size.x, (int)panel.size.y);
			glEnable(GL_SCISSOR_TEST);
			
			useBatches(panel, g);
			
			glDisable(GL_SCISSOR_TEST);
		}
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
	
	private void useBatches(GUIObject panel, Graphics g) {
		
		for(GUIObject gui : ((Panel)panel).panel_objects) {
			if(gui instanceof Panel) {
				useBatches(gui, g);
			}
		}
		
		g.getBatch(PolygonBatch.class).begin(GL_TRIANGLES);
		for(GUIObject gui : ((Panel)panel).panel_objects) {
			if(gui instanceof Button) {
				if(((Button) gui).show_button) {
					g.getBatch(PolygonBatch.class).draw(((Button) gui).poly, gui.pos.x, gui.pos.y, gui.size.x, gui.size.y, gui.color);
				}
			}
		}
		g.getBatch(PolygonBatch.class).end();
		
		g.getBatch(TextBatch.class).begin();
		for(GUIObject gui : ((Panel)panel).panel_objects) {
			if(gui instanceof Button) {
				if(!((Button) gui).label.isBlank() && ((Button)gui).show_label) {
					Vector2f final_pos = ((Button) gui).label_pos.add(gui.pos, new Vector2f());
					g.getBatch(TextBatch.class).draw(font, ((Button) gui).label, final_pos.x, final_pos.y, ((Button) gui).label_color.x,  ((Button) gui).label_color.y, ((Button) gui).label_color.z, ((Button) gui).label_color.w);
				}
			}
		}
		g.getBatch(TextBatch.class).end();
		
		g.getBatch(SpriteBatch.class).begin();
		for(GUIObject gui : ((Panel)panel).panel_objects) {
			if(gui instanceof Image) {
				if(((Image) gui).image != null) {
					g.getBatch(SpriteBatch.class).draw(((Image) gui).image, gui.pos.x, gui.pos.y, gui.size.x, gui.size.y, gui.color);
				}
			}
		}
		g.getBatch(SpriteBatch.class).end();
	}
}
