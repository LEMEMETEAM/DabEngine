import org.lwjgl.nanovg.NVGColor;

import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.nanovg.NanoVG.*;

public class Gui {

    private long vg;
    private NVGColor color;
    private float windowHeight, windowWidth;

    public Gui(float windowWidth, float windowHeight){
        vg = nvgCreate(NVG_STENCIL_STROKES);
        color = NVGColor.create();

        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    public void drawText(String text, float x, float y, String fontType, String fontLoc){
        int font = nvgCreateFont(vg, fontType, fontLoc);
        if(font == -1){
            System.out.println("Couldn't find font");
        }

        nvgBeginFrame(vg, windowWidth, windowHeight, 1);
        nvgFontSize(vg, 16);
        nvgFontFace(vg, "BOLD");
        nvgFillColor(vg, nvgRGBAf(1f, 1f, 1f, 1f, color));
        nvgText(vg, x, y, text);
        nvgEndFrame(vg);
    }
}
