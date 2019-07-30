package DabEngine.Graphics.Models;

import static org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;

import DabEngine.Graphics.OpenGL.Shaders.Shaders;

public class UniformBuffer {

    private int ubo;
    private UniformAttribs[] attribs;
    private int totalComponents;
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
        buffer = BufferUtils.createFloatBuffer(totalComponents);
        ubo = glGenBuffers();
        glBindBuffer(GL_UNIFORM_BUFFER, ubo);
        glBufferData(GL_UNIFORM_BUFFER, buffer, GL_STATIC_DRAW);
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    public void bindToShader(Shaders s){
        int idx = glGetUniformBlockIndex(s.getProgram(), block_name);
        int point = getBindingPoint();
        glUniformBlockBinding(s.getProgram(), idx, point);
        glBindBufferBase(GL_UNIFORM_BUFFER, point, ubo); 
        binds.put(point, block_name);
    }

    private int getBindingPoint(){
        for(var e : binds.entrySet()){
            if(e.getValue() == block_name){
                return e.getKey();
            }
        }
        return bindingPoint++;
    }

    public UniformBuffer put(int pos, float[] f){
        buffer.position(pos);
        buffer.put(f);
        return this;    
    }

    public void flush(){
        glBindBuffer(GL_UNIFORM_BUFFER, ubo);
        glBufferSubData(GL_UNIFORM_BUFFER, 0, buffer.flip());
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

}