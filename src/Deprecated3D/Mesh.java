package Deprecated3D;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL40.*;

public class Mesh {

    private int drawcount;
    private int va_id;
    private int vo_id;
    private int t_id;
    private int i_id;

    private float[] verts;

    public Mesh(float[] verts, float[] tex_cords, int[] indices){
        drawcount = indices.length;

        FloatBuffer vertexbuffer = BufferUtils.createFloatBuffer(verts.length);
        vertexbuffer.put(verts);
        vertexbuffer.flip();

        FloatBuffer texturebuffer = BufferUtils.createFloatBuffer(tex_cords.length);
        texturebuffer.put(tex_cords);
        texturebuffer.flip();

        IntBuffer indexbuffer = BufferUtils.createIntBuffer(indices.length);
        indexbuffer.put(indices);
        indexbuffer.flip();

        va_id = glGenVertexArrays();
        glBindVertexArray(va_id);

        vo_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vo_id);
        glBufferData(GL_ARRAY_BUFFER, vertexbuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);


        t_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, t_id);
        glBufferData(GL_ARRAY_BUFFER, texturebuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        i_id = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexbuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        this.verts = verts;
    }

    public void render(){
        glBindVertexArray(va_id);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, drawcount, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

    }

    public float[] getVerts() {
        return verts;
    }
}
