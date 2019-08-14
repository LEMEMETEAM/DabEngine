package DabEngine.Graphics;

import java.nio.FloatBuffer;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Stack;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.system.MemoryStack;

import DabEngine.Core.App;
import DabEngine.Core.Engine;
import DabEngine.Core.IDisposable;
import DabEngine.Graphics.Batch.Batch;
import DabEngine.Graphics.Batch.Font;
import DabEngine.Graphics.Models.Model;
import DabEngine.Graphics.Models.UniformBuffer;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Graphics.OpenGL.Textures.Texture;
import DabEngine.Graphics.OpenGL.Textures.TextureRegion;
import DabEngine.Graphics.OpenGL.Viewport.Viewport;
import DabEngine.Utils.Color;
import DabEngine.Utils.Pair;
import DabEngine.Graphics.OpenGL.*;
import DabEngine.Graphics.OpenGL.Light.Light;

import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.opengl.GL11.*;

public class Graphics implements IDisposable{

    public static final Texture WHITE_PIXEL = new Texture(1, 1, true);
    /**
     * The vertex batch to use
     */
    private Batch batch;
    /**
     * Stack of shaders. Can push shaders to the stack to be 
     * used by the {@see VertexBatch} and also pop them off to
     * revert back to a previous shader.
     */
    private ArrayDeque<Shaders> shaderStack;
    /**
     * The {@see RenderTarget} to render to.
     */
    private RenderTarget RenderTarget;
    private App app;
    private Camera cam;

    public Graphics(App app){
        batch = new Batch(Batch.DEFAULT_SHADER, new Matrix4f().setOrtho2D(0, app.WIDTH, app.HEIGHT, 0));
        shaderStack = new ArrayDeque<>();
        shaderStack.push(Batch.DEFAULT_SHADER);
        this.app = app;
    }

    public void pushShader(Shaders s){
        shaderStack.push(s);
        batch.setShader(shaderStack.peek());
    }

    public void popShader(){
        batch.setShader(shaderStack.pop());
    }

    public void setBlend(Blending blend){
        batch.setBlend(blend);
    }

    public void setRenderTarget(RenderTarget r){
        if(r != RenderTarget){
            end();
            begin(r);
        }
    }

    public void setCamera(Camera camera){
        batch.cam = camera;
        batch.setProjectionMatrix(camera.getProjection());
    }

    public void begin(RenderTarget r, boolean... batched){
        if(r != null){
            r.bind();
            RenderTarget = r;
        }
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
        batch.begin(batched == null ? true : batched[0]);
    }

    public void drawLine(float x0, float y0, float x1, float y1, float depth, float thickness, Color c) {
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

        batch.setTexture(new Pair<>(WHITE_PIXEL, 0));
        batch.addQuad(x0 + wy, y0 - wx, depth, length, thickness, 0, 0, rotation, c, 0, 0, 1, 1);
    }

    public void drawCharacter(Font f, char c, FloatBuffer x, FloatBuffer y, float depth, Color col) {
        try (MemoryStack stack = stackPush()) {

            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);

            f.getData().position(0);

            stbtt_GetPackedQuad(f.getData(), 512, 512, c, x, y, q, f.integer_align);

            float x0, y0, x1, y1;
            x0 = q.x0();
            y0 = q.y0();
            x1 = q.x1();
            y1 = q.y1();

            batch.setTexture(new Pair<>(f.getTexture(), 0));
            batch.addQuad(x0, y0, depth, x1 - x0, y1 - y0, 0, 0, 0, col, q.s0(), q.t0(), q.s1(), q.t1());
        }
    }

    public void drawText(Font f, String s, float x, float y, float depth, Color col) {
        try (MemoryStack stack = stackPush()) {
            FloatBuffer pX = stack.floats(x);
            FloatBuffer pY = stack.floats(y);

            for (int c = 0; c < s.length(); c++) {
                drawCharacter(f, s.charAt(c), pX, pY, depth, col);
            }
        }
    }

    public void drawRect(float x, float y, float depth, float width, float height, float thickness, Color c) {
        drawLine(x, y, x + width, y, depth, thickness, c);
        drawLine(x + width, y, x + width, y + height, depth, thickness, c);
        drawLine(x + width, y + height, x, y + height, depth, thickness, c);
        drawLine(x, y + height, x, y, depth, thickness, c);
    }

    public void fillRect(float x, float y, float depth, float width, float height, float ox, float oy, float rotation, Color c) {
        batch.setTexture(new Pair<>(WHITE_PIXEL, 0));
        batch.addQuad(x, y, depth, width, height, ox, oy, rotation, c, 0, 0, 1, 1);
    }

    public void drawTexture(Texture tex, TextureRegion region, float x, float y, float depth, float width, float height, float ox, float oy,
            float rotation, Color c) {
        batch.setTexture(new Pair<>(tex, 0));
        if(region != null)
            batch.addQuad(x, y, depth, width, height, ox, oy, rotation, c, region.getUV().x, region.getUV().y, region.getUV().z, region.getUV().w);
        else
            batch.addQuad(x, y, depth, width, height, ox, oy, rotation, c, 0, 0, 1, 1);
    }

    public void draw(float data[], float x, float y, float z, float scale, float rotation, Vector3f rotationAxis){
        float[] temp = Arrays.copyOf(data, data.length);
        rotation = (float)Math.toRadians(rotation);
        for(int i = 0; i < temp.length/12; i++){
            float pX = temp[i*12+0]*scale;
            float pY = temp[i*12+1]*scale;
            float pZ = temp[i*12+2]*scale;

            float tx = 0, ty = 0, tz = 0;
            tx = pX + x;
            ty = pY + y;
            tz = pZ + z;

            float rx = 0, ry = 0, rz = 0;
            if(rotationAxis.z == 1){
                rx = tx*(float)Math.cos(rotation) - ty*(float)Math.sin(rotation);
                ry = tx*(float)Math.sin(rotation) + ty*(float)Math.cos(rotation);
                rz = tz;
            }
            else if(rotationAxis.y == 1){
                rx = tx*(float)Math.cos(rotation) + tz*(float)Math.sin(rotation);
                ry = ty;
                rz = tx*-(float)Math.sin(rotation) + tz*(float)Math.cos(rotation);
            }
            else if(rotationAxis.x == 1){
                rx = tx;
                ry = ty*(float)Math.cos(rotation) - tz*(float)Math.sin(rotation);
                rz = ty*(float)Math.sin(rotation) + tz*(float)Math.cos(rotation);
            }

            temp[i*12+0] = rx;
            temp[i*12+1] = ry;
            temp[i*12+2] = rz;
        }

        batch.add(temp);
    }

    public void end() {
        batch.end();
        if (RenderTarget != null) {
            RenderTarget.unbind();
            glDisable(GL_DEPTH_TEST);
            glClear(GL_COLOR_BUFFER_BIT);
            //glViewport(0,0,engine.getMainWindow().getFramebufferWidth(),engine.getMainWindow().getFramebufferHeight());
            RenderTarget.blit();
            //RenderTarget = null;
        }
    }

    public Shaders getCurrentShader(){
        return shaderStack.peek();
    }

    public Batch getBatch(){
        return batch;
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}