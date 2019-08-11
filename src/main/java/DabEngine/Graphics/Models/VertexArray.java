package DabEngine.Graphics.Models;

import static org.lwjgl.opengl.GL33.*;
public class VertexArray {
    private int vao;
    private VertexAttrib[] attribs;
    public VertexBuffer buffer;

    public VertexArray(int vert_size, VertexAttrib... attribs){
        this.attribs = attribs;
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        buffer = new VertexBuffer(vert_size, attribs);
        glBindVertexArray(0);
    }

    public void bind(){
        glBindVertexArray(vao);
        for(VertexAttrib att:attribs){
            glEnableVertexAttribArray(att.location);
        }
    }

    public void unbind(){
        for(VertexAttrib att:attribs){
            glDisableVertexAttribArray(att.location);
        }
        glBindVertexArray(0);
    }
}