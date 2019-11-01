package DabEngine.Graphics;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.stb.STBTruetype.stbtt_GetPackedQuad;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.system.MemoryStack;

import DabEngine.Core.App;
import DabEngine.Core.AppAdapter;
import DabEngine.Core.IDisposable;
import DabEngine.Graphics.Batch.Batch;
import DabEngine.Graphics.Models.UniformAttribs;
import DabEngine.Graphics.Models.UniformBuffer;
import DabEngine.Graphics.Blending;
import DabEngine.Resources.ResourceManager;
import DabEngine.Resources.Font.Font;
import DabEngine.Resources.Shaders.Shaders;
import DabEngine.Resources.Textures.Texture;
import DabEngine.Resources.Textures.TextureRegion;
import DabEngine.Utils.Color;

public class Graphics implements IDisposable 
{

    private Batch batch;
    private Shaders currentShader;
    private Texture[] currentTextureSlots;
    private Matrix4f mat;
    private Blending blending;
    private App app;
    private UniformBuffer uniformbuffer;

    public Graphics(App app)
    {
        batch = new Batch(1024);

        uniformbuffer = new UniformBuffer("globals", new UniformAttribs(0, "matrix", 16));

        currentTextureSlots = new Texture[16];

        this.app = app;
        
    }

    private void updateUniforms()
    {
        try(MemoryStack stack = MemoryStack.stackPush())
        {
            FloatBuffer b = stack.mallocFloat(16);
            mat.get(b);
            uniformbuffer.put(0, b);
        }
        //TODO: temp
        for(int i = 0; i < currentTextureSlots.length; i++)
        {
            currentShader.setUniform1i("texture"+i, i);
        }
    }

    public void begin()
    {
        batch.begin();

        if(mat == null) mat = new Matrix4f().ortho2D(0, app.getWindow().getWidth(true), app.getWindow().getHeight(true), 0);

        currentTextureSlots[0] = ResourceManager.defaultTexture;
        currentShader = ResourceManager.defaultShaders;

        currentShader.bind();
        uniformbuffer.bindToShader(currentShader);

        updateUniforms();
    }

    public void end()
    {
        flush();
        batch.end();
    }

    private void flush()
    {
        uniformbuffer.flush();
        if(blending != null) blending.apply();
        for(int i = 0; i < currentTextureSlots.length; i++)
        {
            if(currentTextureSlots[i] != null)
                currentTextureSlots[i].bind(i);
        }
        batch.flush();
        for(int i = 0; i < currentTextureSlots.length; i++)
        {
            if(currentTextureSlots[i] != null)
                currentTextureSlots[i].unbind();
        }
    }

    private void checkFlush()
    {
        if(batch.getIdx() >= batch.getSize())
        {
            flush();
        }
    }

    //
    //TEXTURES
    //

    public void setTexture(int unit, Texture tex)
    {
        if(tex != currentTextureSlots[unit] && batch.hasBegun())
        {
            flush();
            if(unit == 0 && tex == null)
            {
                currentTextureSlots[unit] = ResourceManager.defaultTexture;
                return;
            }
            currentTextureSlots[unit] = tex;
        }
    }

    public void setTexture(Texture tex)
    {
        setTexture(0, tex);
    }

    public Texture getCurrentTexture(int unit)
    {
        return currentTextureSlots[unit];
    }

    public Texture getCurrentTexture()
    {
        return getCurrentTexture(0);
    }

    //----------------------------------------------------------------------

    //
    //SHADERS
    //

    /**
     * @param currentShader the currentShader to set
     */
    public void setCurrentShader(Shaders currentShader) {
        if(this.currentShader != currentShader && batch.hasBegun())
        {
            flush();
            this.currentShader  = currentShader == null ? ResourceManager.defaultShaders : currentShader;
            currentShader.bind();
            uniformbuffer.bindToShader(currentShader);
            updateUniforms();
        }
    }

    /**
     * @return the currentShader
     */
    public Shaders getCurrentShader() {
        return currentShader;
    }

    //----------------------------------------------------------------------------

    //
    //DRAWING
    //
    public void draw(float[] data, Vector3f pos, Vector3f scale, Vector3f rot_origin, Vector4f rotation, Color color)
    {
        checkFlush();

        Matrix4f model = new Matrix4f();
        model.translate(pos);
        model.scale(scale);
        model.rotateAround(new Quaternionf().rotateAxis(rotation.w, rotation.x==0&&rotation.y==0&&rotation.z==0 ? 1 : rotation.x, rotation.y, rotation.z), rot_origin.x, rot_origin.y, rot_origin.z);

        float[] d = new float[data.length];
        System.arraycopy(data, 0, d, 0, data.length);

        for(int i = 0; i < data.length/12; i++)
        {
            Vector3f verts = new Vector3f(d[i*12+0], d[i*12+1], d[i*12+2]).mulPosition(model);
            d[i*12+0] = verts.x;
            d[i*12+1] = verts.y;
            d[i*12+2] = verts.z;
        }

        batch.add(d);
    }

    public void drawQuad(TextureRegion region, Vector3f pos, Vector3f scale, Vector3f rot_origin, Vector4f rotation, Color color)
    {
        if(region == null)
        {
            region = new TextureRegion().setUV(0, 0, 1, 1);
        }
        Vector3f normals1 = new Vector3f(0, 0, 1);
        Vector3f normals2 = new Vector3f(0, 0, -1);
        float[] data = new float[]{
            -0.5f, -0.5f, 0,
            color.color[0], color.color[1], color.color[2], color.color[3],
            region.getUV().x, region.getUV().y,
            normals1.x, normals1.y, normals1.z,

            -0.5f, 0.5f, 0,
            color.color[0], color.color[1], color.color[2], color.color[3],
            region.getUV().x, region.getUV().w,
            normals1.x, normals1.y, normals1.z,

            0.5f, 0.5f, 0,
            color.color[0], color.color[1], color.color[2], color.color[3],
            region.getUV().z, region.getUV().w,
            normals1.x, normals1.y, normals1.z,

            0.5f, 0.5f, 0,
            color.color[0], color.color[1], color.color[2], color.color[3],
            region.getUV().z, region.getUV().w,
            normals2.x, normals2.y, normals2.z,

            0.5f, -0.5f, 0,
            color.color[0], color.color[1], color.color[2], color.color[3],
            region.getUV().z, region.getUV().y,
            normals2.x, normals2.y, normals2.z,

            -0.5f, -0.5f, 0,
            color.color[0], color.color[1], color.color[2], color.color[3],
            region.getUV().x, region.getUV().y,
            normals2.x, normals2.y, normals2.z
        };

        draw(data, pos, scale, rot_origin, rotation, color);
    }

    public void drawQuad(Vector3f pos, Vector3f scale, Vector3f rot_origin, Vector4f rotation, Color color)
    {
        drawQuad(null, pos, scale, rot_origin, rotation, color);
    }

    public void drawText(Font font, String s, Vector3f pos, Color color)
    {
        setTexture(font.getTexture());
        try(MemoryStack stack = MemoryStack.stackPush())
        {
            FloatBuffer x = stack.floats(pos.x);
            FloatBuffer y = stack.floats(pos.y);

            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);

            for (int c = 0; c < s.length(); c++) {

        
                font.getData().position(0);
                char cp = s.charAt(c);

                if(cp == '\n')
                {
                    y.put(0, y.get(0) + (font.getAscent() - font.getDescent() + font.getLineGap()) * font.getScale());
                    x.put(0, pos.x);
                    continue;
                }
                else if(cp < 32 || cp >= 128)
                {
                    continue;
                }
    
                stbtt_GetPackedQuad(font.getData(), 512, 512, cp, x, y, q, font.align_to_int);
    
                float x0, y0, x1, y1;
                x0 = q.x0();
                y0 = q.y0();
                x1 = q.x1();
                y1 = q.y1();
                
                batch.add(
                    new float[]{
                        x0, y0, pos.z,
                        color.getColor()[0], color.getColor()[1], color.getColor()[2], color.getColor()[3],
                        q.s0(), q.t0(),
                        0, 0, 1,

                        x0, y1, pos.z,
                        color.getColor()[0], color.getColor()[1], color.getColor()[2], color.getColor()[3],
                        q.s0(), q.t1(),
                        0, 0, 1,

                        x1, y1, pos.z,
                        color.getColor()[0], color.getColor()[1], color.getColor()[2], color.getColor()[3],
                        q.s1(), q.t1(),
                        0, 0, 1,

                        x1, y1, pos.z,
                        color.getColor()[0], color.getColor()[1], color.getColor()[2], color.getColor()[3],
                        q.s1(), q.t1(),
                        0, 0, -1,

                        x1, y0, pos.z,
                        color.getColor()[0], color.getColor()[1], color.getColor()[2], color.getColor()[3],
                        q.s1(), q.t0(),
                        0, 0, -1,

                        x0, y0, pos.z,
                        color.getColor()[0], color.getColor()[1], color.getColor()[2], color.getColor()[3],
                        q.s0(), q.t0(),
                        0, 0, -1,
                    }
                );
                
            }
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    //
    //BLENDING
    //
    public void setBlendMode(Blending b)
    {
        if(b != null)
        {
            glEnable(GL_BLEND);
        }
        else
        {
            glDisable(GL_BLEND);
        }

        if(blending != b && batch.hasBegun())
        { 
            flush();
            blending = b;
        }
    }

    //---------------------------------------

    //
    //MATRIX
    //
    public void setMatrix(Matrix4f m)
    {
        if(batch.hasBegun())
        {
            flush();
            if(m == null) mat = new Matrix4f().ortho2D(0, app.getWindow().getWidth(true), app.getWindow().getHeight(true), 0);
            else mat = m;
            updateUniforms();
        }
        else
        {
            if(m == null) mat = new Matrix4f().ortho2D(0, app.getWindow().getWidth(true), app.getWindow().getHeight(true), 0);
            else mat = m;
        }
    }

    //----------------------------------------
    
    @Override
    public void dispose() 
    {
        // TODO Auto-generated method stub
        batch.dispose();
        for(int i = 0; i < currentTextureSlots.length; i++)
        {
            if(currentTextureSlots[i] != null) currentTextureSlots[i].dispose();
        }
        
    }

    
}