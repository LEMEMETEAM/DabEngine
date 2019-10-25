package DabEngine.Graphics.Models;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;
import static org.lwjgl.opengl.GL31.glGetUniformBlockIndex;
import static org.lwjgl.opengl.GL31.glUniformBlockBinding;
import static org.lwjgl.system.MemoryUtil.memCallocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map.Entry;

import DabEngine.Core.IDisposable;
import DabEngine.Resources.Shaders.Shaders;

public class UniformBuffer implements IDisposable {

    private int ubo;
    private UniformAttribs[] attribs;
    public int totalComponents;
    private String block_name;
    private static int bindingPoint;
    private static HashMap<Integer, String> binds = new HashMap<>();
    private FloatBuffer buffer;
    private Shaders lastShader;

    public UniformBuffer(String block_name, UniformAttribs... attribs){
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
        for(Entry<Integer, String> e : binds.entrySet()){
            if(e.getValue().equals(block_name)){
                return e.getKey();
            }
        }
        return bindingPoint++;
    }

    public UniformBuffer put(int pos, float f){
        buffer.put(pos, f);
        return this;    
    }

    public UniformBuffer put(int num, FloatBuffer f){
        int orig = buffer.position();
        ((FloatBuffer)buffer.position(attribs[num].pos)).put(f);
        buffer.position(orig);
        return this;    
    }

    public UniformBuffer put(int num, float[] f){
        int orig = buffer.position();
        ((FloatBuffer)buffer.position(attribs[num].pos)).put(f);
        buffer.position(orig);
        return this;    
    }

    public void flush(){
        glBindBuffer(GL_UNIFORM_BUFFER, ubo);
        glBufferSubData(GL_UNIFORM_BUFFER, 0, (FloatBuffer)buffer.rewind());
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    @Override
    public void dispose() {
        memFree(buffer);
    }

}