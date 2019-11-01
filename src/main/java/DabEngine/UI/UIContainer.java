package DabEngine.UI;

import java.util.ArrayList;

import org.joml.Vector3f;
import org.joml.Vector4f;

import DabEngine.Graphics.Graphics;
import DabEngine.Input.KeyEvent;
import DabEngine.Input.Keyboard;
import DabEngine.Input.Mouse;
import DabEngine.Observer.Event;
import DabEngine.Utils.Color;

public class UIContainer extends UIElement {

    public static boolean DEBUG;

    public UIContainer(float x, float y, float sizex, float sizey) {
        super(x, y, sizex, sizey);
        // TODO Auto-generated constructor stub
    }

    private ArrayList<UIElement> elements = new ArrayList<>();

    public void addElement(UIElement e) {
        e.setParent(this);
        e.setPos(e.getPos().x, e.getPos().y);
        elements.add(e);
    }

    public void removeElement(UIElement e) {
        elements.remove(e);
    }

    @Override
    public void onKeyDown(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onKeyUp(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNotify(Event e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void draw(Graphics g) {
        // TODO Auto-generated method stub
        if(DEBUG)
        {
            g.drawQuad(new Vector3f(pos, 0), new Vector3f(size, 0), new Vector3f(0), new Vector4f(1,0,0,0), Color.WHITE);
        }
        for(UIElement e : elements)
        {
            if(e != null)
            {
                e.draw(g);
            }
        }
    }

    public void observe(Mouse m, Keyboard k)
    {
        for(UIElement e : elements)
        {
            if(m != null)
            {
                m.addObserver(e);
            }
            
            if(k != null)
            {
                k.addObserver(e);
            }
        }
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        for(UIElement e : elements)
        {
            if(e != null)
            {
                e.update();
            }
        }
    }

}