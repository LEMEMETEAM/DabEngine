package Deprecated3D;

import org.joml.Vector4f;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nuklear.NkAllocator;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkUserFont;

import Core.Engine;
import Utils.Utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Vector;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;

public class GUI {

	private long vg;
	private NVGColor nvgcolor;
	
    public GUI(){
    	vg = nvgCreate(NVG_STENCIL_STROKES);
    	
    	if (vg == 0) {
            throw new RuntimeException("Could not init nanovg.");
        }
    	
    	nvgcolor = NVGColor.create();
    }
    
    public void begin() {
    	nvgBeginFrame(vg, Engine.getMainWindow().getWidth(), Engine.getMainWindow().getHeight(), 1);
    }
    
    public void drawText(String fontLoc, int size, float x, float y, String text, Vector4f color) {
    	int font = nvgCreateFont(vg, "BOLD", fontLoc);
        if (font == -1) {
            throw new RuntimeException("Could not add font");
        }
        
        nvgBeginPath(vg);
        nvgFontFace(vg, "BOLD");
        nvgFontSize(vg, size);
        nvgFillColor(vg, nvgRGBAf(color.x, color.y, color.z, color.w, nvgcolor));
        nvgText(vg, x, y, text);
    }
    
    public void drawText(String fontLoc, int size, float x, float y, int align, String text, Vector4f color) {
        int font = nvgCreateFont(vg, "BOLD", fontLoc);
        if (font == -1) {
            throw new RuntimeException("Could not add font");
        }
        
        nvgBeginPath(vg);
        nvgFontFace(vg, "BOLD");
        nvgFontSize(vg, size);
        nvgTextAlign(vg, align);
        nvgFillColor(vg, nvgRGBAf(color.x, color.y, color.z, color.w, nvgcolor));
        nvgText(vg, x, y, text);
    }
    
    public void drawTextBox(String fontLoc, int size, float x, float y, String text, float boxwidth, Vector4f color) {
    	int font = nvgCreateFont(vg, "BOLD", fontLoc);
        if (font == -1) {
            throw new RuntimeException("Could not add font");
        }
        
        nvgBeginPath(vg);
        nvgFontFace(vg, "BOLD");
        nvgFontSize(vg, size);
        nvgFillColor(vg, nvgRGBAf(color.x, color.y, color.z, color.w, nvgcolor));
        nvgTextBox(vg, x, y, boxwidth, text);
    }
    
    public void drawTextBox(String fontLoc, int size, float x, float y, int align, float boxwidth, String text, Vector4f color) {
        int font = nvgCreateFont(vg, "BOLD", fontLoc);
        if (font == -1) {
            throw new RuntimeException("Could not add font");
        }
        
        nvgBeginPath(vg);
        nvgFontFace(vg, "BOLD");
        nvgFontSize(vg, size);
        nvgTextAlign(vg, align);
        nvgFillColor(vg, nvgRGBAf(color.x, color.y, color.z, color.w, nvgcolor));
        nvgTextBox(vg, x, y, boxwidth, text);
    }
    
    public void drawRect(float x, float y, float width, float height, Vector4f color) {
    	nvgBeginPath(vg);
    	nvgRect(vg, x, y, width, height);
    	nvgFillColor(vg, nvgRGBAf(color.x, color.y, color.z, color.w, nvgcolor));
    	nvgFill(vg);
    }
    
    public void end() {
    	nvgEndFrame(vg);
    }
}