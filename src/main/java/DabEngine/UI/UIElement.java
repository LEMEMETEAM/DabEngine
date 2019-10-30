package DabEngine.UI;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import DabEngine.Graphics.Graphics;
import DabEngine.Input.KeyboardEventListener;
import DabEngine.Input.MouseEvent;
import DabEngine.Input.MouseEventListener;
import DabEngine.Input.MouseMoveEvent;

public abstract class UIElement implements KeyboardEventListener, MouseEventListener
{

    protected interface EventCallback {public void call();}
    public interface OnInsideCallback extends EventCallback{}
    protected Vector2f pos;
    protected Vector2f size;
    protected Vector2f anchor;

    protected boolean visible;
    protected boolean active, keepActive;
    protected boolean busy, enabled;
    
    private boolean MOUSE_INSIDE, PRESSED;

    protected UIElement parent;

    public UIElement(float x, float y, float sizex, float sizey)
    {
        pos = new Vector2f(x, y);
        size = new Vector2f(sizex, sizey);
        anchor = new Vector2f();

        visible = true;
        active = false;
        keepActive = false;
        enabled = true;
        busy = false;

        parent = null;
    }

    public void setParent(UIElement parent) 
    {
        this.parent = parent;
    }

    public void setVisible(boolean visible) 
    {
        this.visible = visible;
    }

    public void setPos(float x, float y) 
    {
        if(parent != null)
        {
            pos.set((parent.getPos().x + x) - size.x * anchor.x, (parent.getPos().y + y) - size.y * anchor.y);
        }
        else
        {
            pos.set(x - size.x * anchor.x, y - size.y * anchor.y);
        }
        onMove();
    }

    public Vector2f getPos() 
    {
        return pos;
    }

    public void setSize(float x, float y) 
    {
        pos.set(pos.x + (size.x - x) * anchor.x, pos.y + (size.y - y) * anchor.y);
        size.set(x, y);
        onResize();
    }

    public Vector2f getSize() 
    {
        return size;
    }

    public void setAnchor(float x, float y) 
    {
        pos.set(pos.x + size.x * (anchor.x - x), pos.y + size.y * (anchor.y - y));
        anchor.set(x, y);
    }

    @Override
    public void onMouseMove(MouseMoveEvent e) {
        // TODO Auto-generated method stub
        Vector2d m_pos = e.getMousePos();
        //is mouse inside element
        if(m_pos.x >= (pos.x+1)-(size.x/2-1) && m_pos.x <= (pos.x+1) + (size.x/2-1) && m_pos.y >= (pos.y+1) - (size.y/2-1) && m_pos.y <= (pos.y+1) + (size.y/2-1))
        {
            if(MOUSE_INSIDE == false)
            {
                MOUSE_INSIDE = true;
                if(visible && enabled)
                {
                    onMouseInside();
                }
            }
        }
        else
        {
            if(MOUSE_INSIDE == true)
            {
                MOUSE_INSIDE = false;
                if(visible && enabled)
                {
                    onMouseOutside();
                }
            }
        }
        
    }

    @Override
    public void onMouseButtonDown(MouseEvent e) {
        // TODO Auto-generated method stub
        if(e.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT)
        {
            if(MOUSE_INSIDE && !PRESSED)
            {
                active = true;
                PRESSED = true;
                onPress();
            }
            else
            {
                active = false;
            }
        }
    }

    @Override
    public void onMouseButtonUp(MouseEvent e) {
        // TODO Auto-generated method stub
        if(PRESSED)
        {
            PRESSED = false;
            onRelease();
        }
    }

    public abstract void draw(Graphics g);
    public void update()
    {

    }

    public void onMove(){}
    public void onResize(){}
    public void onMouseInside(){}
    public void onMouseOutside(){}
    public void onPress(){}
    public void onRelease(){}

}