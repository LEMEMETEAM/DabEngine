package Graphics;

import org.joml.Vector4f;
import org.lwjgl.nanovg.NVGColor;

import java.util.Vector;

import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.nanovg.NanoVG.*;

public class HUD {

    private long vg;
    private NVGColor color;
    private float windowHeight, windowWidth;

    public HUD(float windowWidth, float windowHeight){
        vg = nvgCreate(NVG_STENCIL_STROKES);
        color = NVGColor.create();

        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    public void begin(Vector4f xywh, Vector4f colour){
      nvgBeginFrame(vg, windowWidth, windowHeight, 1);
      nvgBeginPath(vg);
      nvgFillColor(vg, nvgRGBAf(colour.x, colour.y, colour.z, colour.w, color));
      nvgRect(vg, xywh.x, xywh.y, xywh.z, xywh.w);
      nvgFill(vg);
    }


    public void drawText(String text, float x, float y, String fontType, String fontLoc){
        int font = nvgCreateFont(vg, fontType, fontLoc);
        if(font == -1){
            System.out.println("Couldn't find font");
        }

        nvgFontSize(vg, 16);
        nvgFontFace(vg, "BOLD");
        nvgFillColor(vg, nvgRGBAf(1f, 1f, 1f, 1f, color));
        nvgText(vg, x, y, text);
    }

    public void drawRect(Vector4f xywh, Vector4f colour){
        nvgFillColor(vg, nvgRGBf(colour.x, colour.y, colour.z, color));
        nvgRect(vg, xywh.x, xywh.y, xywh.z, xywh.w);
        nvgFill(vg);
    }

    public void end(){
        nvgEndFrame(vg);
    }
}
