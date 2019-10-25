package DabEngine.Resources.Shaders;

import static org.lwjgl.opengl.GL33.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

import DabEngine.Resources.Resource;

/**
 * class that loads shaders, adds them to opengl and stores the id forthe program, vertex shader and fragment shader
 */
public class Shaders extends Resource {

    private int program, vertex, fragment;
    private String vertexShader, fragmentShader;
    private boolean source;
    private int programBackup;

    public Shaders(String vertexShader, String fragmentShader, boolean source)
    {
        super();

        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;

        program = 0;
        vertex = 0;
        fragment = 0;

        programBackup = 0;

        this.source = source;

    }

    public void bind()
    {
        int[] pb = new int[1];
        glGetIntegerv(GL_CURRENT_PROGRAM, pb);
        programBackup = pb[0];

        glUseProgram(program);
    }

    public void unbind()
    {
        glUseProgram(programBackup);
    }

    public void create()
    {
        ready = compile(vertexShader, fragmentShader, source);
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    public boolean compile(String v, String f, boolean s){
        //TODO: add logger
        vertex = source ? createShaderFromString(v, GL_VERTEX_SHADER) : createShaderFromFile(v, GL_VERTEX_SHADER);
        if(vertex == 0)
        {
            return false;
        }

        fragment = source ? createShaderFromString(f, GL_FRAGMENT_SHADER) : createShaderFromFile(f, GL_FRAGMENT_SHADER);
        if(fragment == 0)
        {
            return false;
        }

        //create program
        program = glCreateProgram();
        if(program == 0){
            //TODO: add logging
            return false;
        }

        //attach
        glAttachShader(program, vertex);
        glAttachShader(program, fragment);

        //link
        glLinkProgram(program);

        //validate
        glValidateProgram(program);

        return true;

    }

    private int createShaderFromFile(String filename, int type) {
        StringBuilder source = new StringBuilder();
        try(BufferedReader b = new BufferedReader(new FileReader(filename)))
        {
            String line;
            while((line = b.readLine()) != null)
            {
                source.append(line).append("\n");
            }
            source.append("\n");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
        return createShaderFromString(source.toString(), type);
    }

    private int createShaderFromString(String source, int type) {
        int shader = glCreateShader(type);

        //compile shader
        glShaderSource(shader, source);
        glCompileShader(shader);

        return shader;
    }

    public void setUniform1i(String name, int value)
    {
        if(!ready) return;

        int id = glGetUniformLocation(program, name);
        if(id > 0)
        {
            glUniform1i(id, value);
        }

    }

    public void setUniform1f(String name, float value)
    {
        if(!ready) return;

        int id = glGetUniformLocation(program, name);
        if(id > 0)
        {
            glUniform1f(id, value);
        }

    }

    public void setUniform2f(String name, Vector2f value)
    {
        if(!ready) return;

        int id = glGetUniformLocation(program, name);
        if(id > 0)
        {
            glUniform2f(id, value.x, value.y);
        }
    }

    public void setUniform3f(String name, Vector3f value)
    {
        if(!ready) return;

        int id = glGetUniformLocation(program, name);
        if(id > 0)
        {
            glUniform3f(id, value.x, value.y, value.z);
        }
    }

    public void setUniform4f(String name, Vector4f value)
    {
        if(!ready) return;

        int id = glGetUniformLocation(program, name);
        if(id > 0)
        {
            glUniform4f(id, value.x, value.y, value.z, value.w);
        }
    }

    public void setUniformMatrix4f(String name, Matrix4f value)
    {
        if(!ready) return;

        int id = glGetUniformLocation(program, name);
        if(id > 0)
        {
            glUniformMatrix4fv(id, false, value.get(MemoryUtil.memAllocFloat(16)));
        }
    }

    /**
     * @return the program
     */
    public int getProgram() {
        return program;
    }

}
