package DabEngine.UI;

import DabEngine.Input.KeyEvent;
import DabEngine.Observer.Event;

interface ButtonClickCallback{ public void call(); }

public class UIButton extends UIElement {

    private ButtonClickCallback callback;

    public UIButton(float x, float y, float sizex, float sizey) {
        super(x, y, sizex, sizey);
        // TODO Auto-generated constructor stub
    }

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
    public void draw() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMouseDownInside() {
        // TODO Auto-generated method stub
        callback.call();
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }

}