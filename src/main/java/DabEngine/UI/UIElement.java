package DabEngine.UI;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import DabEngine.Input.KeyboardEventListener;
import DabEngine.Input.MouseEvent;
import DabEngine.Input.MouseEventListener;
import DabEngine.Input.MouseMoveEvent;

public abstract class UIElement implements KeyboardEventListener, MouseEventListener
{

    protected Vector2f pos;
    protected Vector2f size;
    protected Vector2f anchor;

    protected boolean visible;
    protected boolean active, keepActive;
    protected boolean busy;
    protected boolean mouseInside;

    protected UIElement parent;

    public UIElement(float x, float y, float sizex, float sizey)
    {
        pos = new Vector2f(x, y);
        size = new Vector2f(sizex, sizey);
        anchor = new Vector2f();

        visible = true;
        active = false;
        keepActive = false;
        busy = false;
        mouseInside = false;

        parent = null;
    }

    public UIElement setPosAbsolute(float x, float y)
    {
        if(pos.x != x || pos.y != y)
        {
            pos.x = x;
            pos.y = y;
            onMove();
        }

        return this;
    }

    public UIElement setPosAbsoluteX(float x)
    {
        return setPosAbsolute(x, pos.y);
    }

    public UIElement setPosAbsoluteY(float y)
    {
        return setPosAbsolute(pos.x, y);
    }

    public UIElement setPos(float x, float y)
    {
        return setPosAbsolute(x - size.x * anchor.x, y - size.y * anchor.y);
    }

    public UIElement setPosX(float x)
    {
        return setPosAbsoluteX(x - size.x * anchor.x);
    }

    public UIElement setPosY(float y)
    {
        return setPosAbsoluteY(y - size.y * anchor.y);
    }

    public UIElement setSizeAbsolute(float x, float y)
    {
        if(size.x != x || size.y != y)
        {
            size.x = x;
            size.y = y;
            onResize();
        }

        return this;
    }

    public UIElement setSizeAbsoluteX(float x)
    {
        return setSizeAbsolute(x, size.y);
    }

    public UIElement setSizeAbsoluteY(float y)
    {
        return setSizeAbsolute(size.x, y);
    }

    public UIElement setSize(float x, float y)
    {
        setPosAbsolute(pos.x + (size.x - x) * anchor.x, pos.y + (size.y - y) * anchor.y);
        return setSizeAbsolute(x, y);
    }

    public UIElement setSizeX(float x)
    {
        setPosAbsoluteX(pos.x + (size.x - x) * anchor.x);
        return setSizeAbsoluteX(x);
    }

    public UIElement setSizeY(float y)
    {
        setPosAbsoluteY(pos.y + (size.y - y) * anchor.y);
        return setSizeAbsoluteY(y);
    }

    public UIElement setAnchorAbsolute(float x, float y)
    {
        if(anchor.x != x || anchor.y != y)
        {
            anchor.x = x;
            anchor.y = y;
        }

        return this;
    }

    public UIElement setAnchorAbsoluteX(float x)
    {
        return setAnchorAbsolute(x, anchor.y);
    }

    public UIElement setAnchorAbsoluteY(float y)
    {
        return setAnchorAbsolute(anchor.x, y);
    }

    public UIElement setAnchor(float x, float y)
    {
        setPosAbsolute(pos.x - size.x * (x - anchor.x), pos.y - size.y * (y - anchor.y));
        setPosAbsolute(pos.x - size.x * (x - anchor.x), pos.y - size.y * (y - anchor.y));
        setAnchorAbsolute(x, y);
        updateLayout();
        return this;
    }

    public UIElement setAnchorX(float x)
    {
        setPosAbsoluteX(pos.x - size.x * (x - anchor.x));
        setPosAbsoluteX(pos.x - size.x * (x - anchor.x));
        setAnchorAbsoluteX(x);
        updateLayout();
        return this;
    }

    public UIElement setAnchorY(float y)
    {
        setPosAbsoluteY(pos.y - size.y * (y - anchor.y));
        setPosAbsoluteY(pos.y - size.y * (y - anchor.y));
        setAnchorAbsoluteY(y);
        updateLayout();
        return this;
    }

    public UIElement setVisible(boolean b){visible = b; return this;}
    public UIElement setActive(boolean b){active = b; return this;}
    public UIElement setBusy(boolean b){busy = b; return this;}
    public UIElement setParent(UIElement e){parent = e; return this;}

    public void updateLayout(){if(parent!=null)parent.updateLayout();}

    @Override
    public void onMouseMove(MouseMoveEvent e) {
        // TODO Auto-generated method stub
        Vector2d m_pos = e.getMousePos();
        if(m_pos.x >= pos.x+1 && m_pos.x <= (pos.x+1) + (size.x-1) && m_pos.y >= pos.y+1 && m_pos.y <= (pos.y+1) + (size.y-1))
        {
            if(!mouseInside)
            {
                mouseInside = true;
                if(visible)
                {
                    onMouseInside();
                }
            }
        }
        else
        {
            if(mouseInside)
            {
                mouseInside = false;
                if(visible)
                {
                    onMouseOutside();
                }
            }
        }
    }

    @Override
    public void onMouseButtonDown(MouseEvent e) {
        // TODO Auto-generated method stub
        if(!visible ) return;

        if(e.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT)
        {
            if(mouseInside)
            {
                active = true;
                onMouseDownInside();
            }
            else
            {
                onMouseDownOutside();
            }
        }
    }

    @Override
    public void onMouseButtonUp(MouseEvent e) {
        // TODO Auto-generated method stub
        if(active)
        {
            if(mouseInside)
            {
                onMouseUpInside();
            }
            else{
                onMouseUpOutside();
            }

            if(!keepActive)
                active = false;
        }
    }

    public abstract void draw();
    public abstract void update();

    public void onMove(){}
    public void onResize(){}
    public void onMouseDownInside(){}
    public void onMouseDownOutside(){}
    public void onMouseUpInside(){}
    public void onMouseUpOutside(){}
    public void onMouseInside(){}
    public void onMouseOutside(){}

}