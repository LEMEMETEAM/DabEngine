package Graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
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

    public Shaders(String filename){
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
    
    public Shaders(String filenamevs, String filenamefs){
        program = glCreateProgram();

        vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, readFile(filenamevs));
        glCompileShader(vs);
        if(glGetShaderi(vs, GL_COMPILE_STATUS) == 0){
            System.err.println("Failed compiling vertex shader " + vs);
            System.exit(0);
        }
        glAttachShader(program, vs);

        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs,readFile(filenamefs));
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
            br = new BufferedReader(new FileReader(new File(filename)));
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

    public void setUniform(String uniformName, Matrix4f value){
    	int location = glGetUniformLocation(program, uniformName);
        if(location < 0){
            System.err.println("Could not set uniform " + uniformName + " because location is " + location);
            System.err.println(glGetError());
            System.exit(1);
        }
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4 * 4);
        value.get(buffer);
        glUniformMatrix4fv(location, false, buffer);
    }

    public void setUniform(String uniformName, int value){
    	int location = glGetUniformLocation(program, uniformName);
        if(location < 0){
        	System.err.println("Could not set uniform " + uniformName + " because location is " + location);
            System.exit(1);
        }
        glUniform1i(location, value);
    }
    
    public void setUniform(String uniformName, float value){
    	int location = glGetUniformLocation(program, uniformName);
        if(location < 0){
        	System.err.println("Could not set uniform " + uniformName + " because location is " + location);
            System.exit(1);
        }
        glUniform1f(location, value);
    }
    
    public void setUniform(String uniformName, Vector4f value){
    	int location = glGetUniformLocation(program, uniformName);
        if(location < 0){
        	System.err.println("Could not set uniform " + uniformName + " because location is " + location);
            System.exit(1);
        }
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        value.get(buffer);
        glUniform4fv(location, buffer);
    }
    
    public void setUniform(String uniformName, Vector3f value){
    	int location = glGetUniformLocation(program, uniformName);
        if(location < 0){
        	System.err.println("Could not set uniform " + uniformName + " because location is " + location);
            System.exit(1);
        }
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        value.get(buffer);
        glUniform3fv(location, buffer);
    }
    
    public void setUniform(String uniformName, Vector2f value){
    	int location = glGetUniformLocation(program, uniformName);
        if(location < 0){
        	System.err.println("Could not set uniform " + uniformName + " because location is " + location);
            System.exit(1);
        }
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        value.get(buffer);
        glUniform2fv(location, buffer);
    }
}
