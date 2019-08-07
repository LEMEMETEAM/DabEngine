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

    public UniformBuffer(String block_name, UniformAttribs... attribs){
        this.attribs = attribs;
        this.block_name = block_name;
        for(UniformAttribs attrib : attribs){
            totalComponents += attrib.numComponents;
        }

        buffer = memCallocFloat(totalComponents);

        ubo = glGenBuffers();
        glBindBuffer(GL_UNIFORM_BUFFER, ubo);
        glBufferData(GL_UNIFORM_BUFFER, buffer, GL_DYNAMIC_DRAW);
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    public void bindToShader(Shaders s){
        int point = getBindingPoint();
        if(binds.get(point) != null){
            return;
        }

        int idx = glGetUniformBlockIndex(s.getProgram(), block_name);
        glUniformBlockBinding(s.getProgram(), idx, point);
        glBindBufferBase(GL_UNIFORM_BUFFER, point, ubo); 
        binds.put(point, block_name);
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
        flush();
        return this;    
    }

    public UniformBuffer put(int num, FloatBuffer f){
        int orig = buffer.position();
        buffer.position(attribs[num].pos).put(f);
        flush();
        return this;    
    }

    public UniformBuffer put(int num, float[] f){
        int orig = buffer.position();
        buffer.position(attribs[num].pos).put(f);
        flush();
        return this;    
    }

    private void flush(){
        glBindBuffer(GL_UNIFORM_BUFFER, ubo);
        glBufferSubData(GL_UNIFORM_BUFFER, 0, buffer.flip());
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    @Override
    public void dispose() {
        memFree(buffer);
    }

}