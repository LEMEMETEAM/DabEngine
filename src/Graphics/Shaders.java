package Graphics;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;

public class Shaders {

    private int program;
    private int vs;
    private int fs;
    private HashMap<String, Integer> uniforms;

    public Shaders(String filename){
        uniforms = new HashMap<>();
        program = glCreateProgram();

        vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, readFile(filename + ".vs"));
        glCompileShader(vs);
        if(glGetShaderi(vs, GL_COMPILE_STATUS) == 0){
            System.err.println("Failed compiling vertex shader " + vs);
            System.exit(0);
        }
        glAttachShader(program, vs);

        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs,readFile(filename + ".fs"));
        glCompileShader(fs);
        if(glGetShaderi(fs, GL_COMPILE_STATUS) == 0){
            System.err.println("Failed compiling fragment shader " + fs);
            System.exit(0);
        }
        glAttachShader(program, fs);

        glLinkProgram(program);
        glValidateProgram(program);

    }

    private String readFile(String filename){
        StringBuilder string = new StringBuilder();
        BufferedReader br;
        try{
            br = new BufferedReader(new FileReader(new File("shaders/" + filename)));
            String line;
            while((line = br.readLine()) != null){
                string.append(line);
                string.append("\n");
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return string.toString();
    }

    public void bind(){
        glUseProgram(program);
    }

    public void unbind(){
        glUseProgram(0);
    }

    public void createUniform(String uniformName){
        int location = glGetUniformLocation(program, uniformName);
        if(location < 0){
            System.err.println("Could not set uniform " + location);
            System.exit(1);
        }
        uniforms.put(uniformName, location);
    }

    public void setUniform(String uniformName, Matrix4f value){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4 * 4);
        value.get(buffer);
        glUniformMatrix4fv(uniforms.get(uniformName), false, buffer);
    }

    public void setUniform(String uniformName, int value){
        glUniform1i(uniforms.get(uniformName), value);
    }
}
