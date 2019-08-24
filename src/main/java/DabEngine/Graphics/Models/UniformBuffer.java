package DabEngine.Graphics.Models;

import static org.lwjgl.opengl.GL33.*;

import java.nio.BufferOverflowException;
import java.nio.FloatBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import static org.lwjgl.system.MemoryUtil.*;

import DabEngine.Core.IDisposable;
import DabEngine.Graphics.OpenGL.Shaders.Shaders;

public class UniformBuffer implements IDisposable {

    private int ubo;
    private UniformAttribs[] attribs;
    public int totalComponents;
    private String block_name;
    private static int bindingPoint;
    private static HashMap<Integer, String> binds = new HashMap<>();
    private FloatBuffer buffer;
    private Shaders lastShader;
    public boolean defered;

    public UniformBuffer(String block_name, boolean defered, UniformAttribs... attribs){
        this.attribs = attribs;
        this.block_name = block_name;
        for(UniformAttribs attrib : attribs){
            totalComponents += attrib.numComponents;
        }

        buffer = memCallocFloat(totalComponents);

        ubo = glGenBuffers();
        glBindBuffer(GL_UNIFORM_BUFFER, ubo);
        glBufferData(GL_UNIFORM_BUFFER, buffer, GL_STATIC_DRAW);
        glBindBuffer(GL_UNIFORM_BUFFER, 0);

        this.defered = defered;
    }

    public void bindToShader(Shaders s){
            int point = getBindingPoint();
            if(binds.get(point) != null && lastShader == s){
                return;
            }

            int idx = glGetUniformBlockIndex(s.getProgram(), block_name);
            glUniformBlockBinding(s.getProgram(), idx, point);
            glBindBufferBase(GL_UNIFORM_BUFFER, point, ubo); 
            binds.put(point, block_name);
            lastShader = s;

    }

    private int getBindingPoint(){
        for(var e : binds.entrySet()){
            if(e.getValue().equals(block_name)){
                return e.getKey();
            }
        }
        return bindingPoint++;
    }

    public UniformBuffer put(int num, float f){
        buffer.put(attribs[num].pos, f);
        if(!defered)
            flush();
        return this;    
    }

    public UniformBuffer put(int num, FloatBuffer f){
        int orig = buffer.position();
        buffer.position(attribs[num].pos).put(f);
        if(!defered)
            flush();
        return this;    
    }

    public UniformBuffer put(int num, float[] f){
        int orig = buffer.position();
        buffer.position(attribs[num].pos).put(f);
        if(!defered)
            flush();
        return this;    
    }

    public void flush(){
        glBindBuffer(GL_UNIFORM_BUFFER, ubo);
        glBufferSubData(GL_UNIFORM_BUFFER, 0, buffer.rewind());
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    @Override
    public void dispose() {
        memFree(buffer);
    }

}