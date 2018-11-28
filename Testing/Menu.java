import Utils.Utils;
import org.lwjgl.nanovg.NVGColor;

import java.nio.ByteBuffer;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;

public class Menu {

    private long vg;
    private NVGColor color;

    public Menu(){
        vg = nvgCreate(NVG_STENCIL_STROKES);

        int font = nvgCreateFont(vg, "BOLD", "./res/OpenSans-Bold.ttf");

        color = NVGColor.create();
    }

    public void render(){
        nvgBeginFrame(vg, 800, 600, 1);
        nvgBeginPath(vg);
        nvgRect(vg, 0,0, 800,600);
        nvgFillColor(vg, nvgRGBAf(0.28f,0.30f,0.34f,0.98f, color));
        nvgFill(vg);

        nvgFontSize(vg, 18);
        nvgFontFace(vg, "BOLD");
        nvgFillColor(vg, nvgRGBAf(1f, 1f,1f, 1f, color));
        nvgText(vg, 0, 20, "Haha lol this was so easy wy dont more ppl use it");

        nvgEndFrame(vg);
    }

}
