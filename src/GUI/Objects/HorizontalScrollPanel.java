package GUI.Objects;

import GUI.GUIObject;
import Input.KeyEvent;

import static org.lwjgl.glfw.GLFW.*;

public class HorizontalScrollPanel extends Panel {
	
	private boolean scroll;
	public static int scroll_key_up = GLFW_KEY_UP;
	public static int scroll_key_down = GLFW_KEY_DOWN;
	private GUIObject lowest;
	private GUIObject highest;
	
	public void checkIfScroll() {
		for(GUIObject g : panel_objects) {
			if(g.pos.y + pos.y > pos.y + size.y) {
				scroll = true;
				if(g.pos.y + pos.y > lowest.pos.y + pos.y) {
					lowest = g;
					continue;
				}
				else if(g.pos.y+pos.y < highest.pos.y + pos.y) {
					highest = g;
					continue;
				}
			}
		}
		if(!scroll) {
			return;
		}
		else {
			scroll = false;
		}
	}
	
	@Override
	public void onKeyPress(KeyEvent e) {
		// TODO Auto-generated method stub
		super.onKeyPress(e);
		if(lowest.pos.y + pos.y <= pos.y + size.y || highest.pos.y + pos.y >= pos.y) {
			return;
		}
		if(e.getKey() == scroll_key_down) {
			for(GUIObject g : panel_objects) {
				g.pos.sub(0, 10);
			}
		}
		else if(e.getKey() == scroll_key_up) {
			for(GUIObject g : panel_objects) {
				g.pos.add(0, 10);
			}
		}
	}
	
	@Override
	public void addToPanel(GUIObject g) {
		// TODO Auto-generated method stub
		super.addToPanel(g);
		checkIfScroll();
	}
}
