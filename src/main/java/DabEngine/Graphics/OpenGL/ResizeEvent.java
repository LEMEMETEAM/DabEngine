package DabEngine.Graphics.OpenGL;

import DabEngine.Observer.Event;

public class ResizeEvent implements Event {
    public int fbWidth, fbHeight;

    public ResizeEvent(int width, int height){
        fbWidth = width;
        fbHeight = height;
    }
}