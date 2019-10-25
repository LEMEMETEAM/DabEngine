package DabEngine.GUI;

import java.util.ArrayList;

import DabEngine.Core.App;
import DabEngine.GUI.GUIObject.States;
import DabEngine.GUI.Objects.Panel;
import DabEngine.Graphics.Graphics;
import DabEngine.Input.InputHandler;

public abstract class AbstractMenu {
	
	public ArrayList<Panel> obj = new ArrayList<>();
	private App app;

	public AbstractMenu(App app){
		this.app = app;
	}
	

	public void update() {
		// TODO Auto-generated method stub
		ArrayList<GUIObject> objs = new ArrayList<>();
		for(Panel p : obj){
			objs.addAll(unpackPanel(p));
		}
		for(GUIObject g : objs){
			checkHover(g);
			g.update();
		}
	}

	private ArrayList<GUIObject> unpackPanel(Panel p){
		ArrayList<GUIObject> unpacked = new ArrayList<>();
		for(GUIObject obj : p.panel_objects){
			if(obj instanceof Panel){
				ArrayList<GUIObject> un = unpackPanel((Panel)obj);
				for(GUIObject o : un){
					unpacked.add(o);
				}
			}
			else{
				unpacked.add(obj);
			}
		}
		return unpacked;
	}

	public void render(Graphics g) {
		// TODO Auto-generated method stub
		for(Panel p : obj){
			p.render(g);
		}
	}
	
	private void checkHover(GUIObject g) {
		if(Math.abs(g.pos.x - (float)InputHandler.INSTANCE.getMousePos().x) * 2 <= (g.size.x + 1) &&
			Math.abs(g.pos.y - (float)InputHandler.INSTANCE.getMousePos().y) * 2 <= (g.size.y + 1)){
			g.state.setState(States.HOVER);
		}
		else{
			g.state.setState(States.EXIT);
		}
	}
}
