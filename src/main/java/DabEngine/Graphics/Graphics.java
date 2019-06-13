package DabEngine.Graphics;

import java.nio.FloatBuffer;
import java.util.Stack;

import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.system.MemoryStack;

import DabEngine.Graphics.Batch.Font;
import DabEngine.Graphics.Batch.QuadBatch;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Graphics.OpenGL.Textures.TextureRegion;
import DabEngine.Utils.Color;
import DabEngine.Utils.Colors;
import DabEngine.Graphics.OpenGL.*;

import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.opengl.GL11.*;

public class Graphics {

    public static final Texture WHITE_PIXEL = new Texture(1, 1, true);
    /**
     * The vertex batch to use
     */
    private QuadBatch batch;
    /**
     * Stack of shaders. Can push shaders to the stack to be 
     * used by the {@see VertexBatch} and also pop them off to
     * revert back to a previous shader.
     */
    private Stack<Shaders> shaderStack;
    /**
     * The {@see RenderTarget} to render to.
     */
    private RenderTarget RenderTarget;

    public Graphics(){
        batch = new QuadBatch(QuadBatch.DEFAULT_SHADER);
        shaderStack = new Stack<>();
        shaderStack.push(QuadBatch.DEFAULT_SHADER);
    }

    public void pushShader(Shaders s){
        batch.setShader(shaderStack.push(s));
    }

    public void popShader(){
        shaderStack.pop();
        batch.setShader(shaderStack.peek());
    }

    public void setRenderTarget(RenderTarget r){
        if(r != RenderTarget){
            end();
            begin(r);
        }
    }

    public void begin(RenderTarget r){
        if(r != null){
            r.bind();
            RenderTarget = r;
            glClear(GL_COLOR_BUFFER_BIT);
        }
        batch.begin();
    }

    public void drawLine(float x0, float y0, float x1, float y1, float thickness, Color c) {
        if (x1 < x0) {
            float temp = x0;
            x0 = x1;
            x1 = temp;
            temp = y0;
            y0 = y1;
            y1 = temp;
        }

        float dx = x1 - x0, dy = y1 - y0;

        float length = (float) Math.sqrt(dx * dx + dy * dy);

        float wx = dx * (thickness / 2) / length;
        float wy = dy * (thickness / 2) / length;

        float rotation = (float) Math.toDegrees((float) Math.atan2(dy, dx));

        batch.addQuad(WHITE_PIXEL, x0 + wy, y0 - wx, length, thickness, 0, 0, rotation, c, 0, 0, 1, 1);
    }

    public void drawCharacter(Font f, char c, FloatBuffer x, FloatBuffer y, Color col) {
        try (MemoryStack stack = stackPush()) {

            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);

            f.getData().position(0);

            stbtt_GetPackedQuad(f.getData(), 512, 512, c, x, y, q, f.integer_align);

            float x0, y0, x1, y1;
            x0 = q.x0();
            y0 = q.y0();
            x1 = q.x1();
            y1 = q.y1();

            batch.addQuad(f.getTexture(), x0, y0, x1 - x0, y1 - y0, 0, 0, 0, col, q.s0(), q.t0(), q.s1(), q.t1());
        }
    }

    public void drawText(Font f, String s, float x, float y, Color col) {
        try (MemoryStack stack = stackPush()) {
            FloatBuffer pX = stack.floats(x);
            FloatBuffer pY = stack.floats(y);

            for (int c = 0; c < s.length(); c++) {
                drawCharacter(f, s.charAt(c), pX, pY, col);
            }
        }
    }

    public void drawRect(float x, float y, float width, float height, float thickness, Color c) {
        drawLine(x, y, x + width, y, thickness, c);
        drawLine(x + width, y, x + width, y + height, thickness, c);
        drawLine(x + width, y + height, x, y + height, thickness, c);
        drawLine(x, y + height, x, y, thickness, c);
    }

    public void fillRect(float x, float y, float width, float height, float ox, float oy, float rotation, Color c) {
        batch.addQuad(WHITE_PIXEL, x, y, width, height, ox, oy, rotation, c, 0, 0, 1, 1);
    }

    public void drawTexture(Texture tex, TextureRegion region, float x, float y, float width, float height, float ox, float oy,
            float rotation, Color c) {
        if(region != null)
            batch.addQuad(tex, x, y, width, height, ox, oy, rotation, c, region.getUV().x, region.getUV().y, region.getUV().z, region.getUV().w);
        else
            batch.addQuad(tex, x, y, width, height, ox, oy, rotation, c, 0, 0, 1, 1);
    }

    public void end() {
        batch.end();
        if (RenderTarget != null) {
            RenderTarget.unbind();
            RenderTarget.blit();
        }
    }
}