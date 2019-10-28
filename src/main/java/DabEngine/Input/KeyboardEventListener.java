package DabEngine.Input;

import DabEngine.Observer.IEventListener;

public interface KeyboardEventListener extends IEventListener 
{

    public void onKeyDown(KeyEvent e);
    public void onKeyUp(KeyEvent e);
    
}