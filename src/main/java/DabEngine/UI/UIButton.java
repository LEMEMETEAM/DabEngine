package DabEngine.UI;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import DabEngine.Graphics.Graphics;
import DabEngine.Input.KeyEvent;
import DabEngine.Observer.Event;
import DabEngine.Resources.Font.Font;
import DabEngine.Utils.Color;

public class UIButton extends UIElement {

    public interface ButtonClickCallback extends EventCallback{}
    private ButtonClickCallback callback;
    protected Color frameColor;
    protected Color backgroundColor;
    private Color backgroundColorLighter, backgroundColorDark, backgroundColorLight, lastBackgroundColor;

    protected boolean drawFrame;
    protected boolean drawBackground;

    protected Font font;
    protected String text;

    public UIButton(float x, float y, float sizex, float sizey, String text) {
        super(x, y, sizex, sizey);
        // TODO Auto-generated constructor stub
        callback = null;
        frameColor = Color.WHITE;
        backgroundColorDark = new Color(0.45f, 0.2f, 0.2f, 1);
        backgroundColorLight = new Color(backgroundColorDark);
        backgroundColorLight.lighten(0.3f);
        backgroundColorLighter = new Color(backgroundColorLight);
        backgroundColorLighter.lighten(0.3f);
        backgroundColor = new Color(backgroundColorDark);


        drawFrame = true;
        drawBackground = true;

        setText(text);
    }

    public void setText(String text){this.text = text;}
    public void setFont(Font font){this.font = font;}

    /**
     * @param callback the callback to set
     */
    public void setCallback(ButtonClickCallback callback) {
        this.callback = callback;
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
        if(!visible) return;

        //draw frame
        if(drawFrame) g.drawQuad(new Vector3f(pos, 0), new Vector3f(size, 0), new Vector3f(0), new Vector4f(0), frameColor);

        //draw background
        if(drawBackground) g.drawQuad(new Vector3f(pos.x, pos.y, 0), new Vector3f(size.x-2, size.y-2, 0), new Vector3f(0),new Vector4f(0), backgroundColor);

        //draw text
        g.drawText(font, text, new Vector3f(pos.x - font.getWidth(text)/2, pos.y + font.getHeight()/2, 0), Color.WHITE);
    }

    @Override
    public void onMouseInside() {
        // TODO Auto-generated method stub
        lastBackgroundColor = backgroundColor;
        backgroundColor = backgroundColorLight;
    }

    @Override
    public void onMouseOutside() {
        // TODO Auto-generated method stub
        backgroundColor = backgroundColorDark;
    }

    @Override
    public void onPress() {
        // TODO Auto-generated method stub
        lastBackgroundColor = backgroundColor;
        backgroundColor = backgroundColorLighter;
    }

    @Override
    public void onRelease() {
        // TODO Auto-generated method stub
        backgroundColor = lastBackgroundColor;
        callback.call();
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }

}