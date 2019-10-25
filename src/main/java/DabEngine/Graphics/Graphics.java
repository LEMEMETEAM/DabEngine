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
import DabEngine.Core.IDisposable;
import DabEngine.Graphics.Batch.Batch;
import DabEngine.Graphics.Models.UniformAttribs;
import DabEngine.Graphics.Models.UniformBuffer;
import DabEngine.Graphics.OpenGL.Blending;
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
    private Camera cam;
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
            cam.getProjection().get(b);
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

        cam = new Camera2D(app.WIDTH, app.HEIGHT);

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
        model.rotateAround(new Quaternionf().rotateAxis(rotation.w, rotation.x, rotation.y, rotation.z), rot_origin.x, rot_origin.y, rot_origin.z);

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
            -1, -1, 0,
            color.color[0], color.color[1], color.color[2], color.color[3],
            region.getUV().x, region.getUV().y,
            normals1.x, normals1.y, normals1.z,

            -1, 1, 0,
            color.color[4], color.color[5], color.color[6], color.color[7],
            region.getUV().x, region.getUV().w,
            normals1.x, normals1.y, normals1.z,

            1, 1, 0,
            color.color[8], color.color[9], color.color[10], color.color[11],
            region.getUV().z, region.getUV().w,
            normals1.x, normals1.y, normals1.z,

            1, 1, 0,
            color.color[8], color.color[9], color.color[10], color.color[11],
            region.getUV().z, region.getUV().w,
            normals2.x, normals2.y, normals2.z,

            1, -1, 0,
            color.color[12], color.color[13], color.color[14], color.color[15],
            region.getUV().z, region.getUV().y,
            normals2.x, normals2.y, normals2.z,

            -1, -1, 0,
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

    public void drawCharacter(Font font, char c, Vector3f pos, Vector3f scale, Vector3f rot_origin, Vector4f rotation, Color color)
    {
        try (MemoryStack stack = MemoryStack.stackPush()) {

            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);

            font.getData().position(0);

            FloatBuffer x = stack.floats(pos.x);
            FloatBuffer y = stack.floats(pos.y);

            stbtt_GetPackedQuad(font.getData(), 512, 512, c, x, y, q, font.integer_align);

            float x0, y0, x1, y1;
            x0 = q.x0();
            y0 = q.y0();
            x1 = q.x1();
            y1 = q.y1();

            setTexture(font.getTexture());
            float width = x1 - x0;
            float height = y1 - y0;
            drawQuad(new Vector3f(x0 - (width/2), y0 - (height/2), pos.z), new Vector3f(width, height, scale.z), rot_origin, rotation, color);
        }
    }

    public void drawText(Font font, String s, Vector3f pos, Vector3f scale, Vector3f rot_origin, Vector4f rotation, Color color)
    {
        for (int c = 0; c < s.length(); c++) {
            drawCharacter(font, s.charAt(c), pos, scale, rot_origin, rotation, color);
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
    //CAMERA
    //
    public void setCamera(Camera cam)
    {
        if(cam == null) this.cam = new Camera2D(app.WIDTH, app.HEIGHT);
        if(this.cam != cam && batch.hasBegun()) 
        {
            flush();
            this.cam = cam;
            updateUniforms();
        }
    }

    /**
     * @return the cam
     */
    public Camera getCamera() 
    {
        return cam;
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