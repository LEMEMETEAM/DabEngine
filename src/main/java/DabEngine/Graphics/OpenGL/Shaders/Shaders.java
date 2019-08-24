package DabEngine.Graphics.OpenGL.Shaders;

import static org.lwjgl.opengl.GL33.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import DabEngine.Cache.InMemoryCache;
import DabEngine.Utils.Pair;

/**
 * class that loads shaders, adds them to opengl and stores the id forthe program, vertex shader and fragment shader
 */
public class Shaders {

    private int program;
    private int vs;
    private int fs;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public String vs_source, fs_source;
    
    public Shaders(File filevs, File filefs, Pair<String, String>... defines){
        this(readFile(filevs, defines), readFile(filefs, defines));
    }
    
    public Shaders(String source_vs, String source_fs) {
    	program = glCreateProgram();

        vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, source_vs);
        glCompileShader(vs);
        glAttachShader(program, vs);

        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs, source_fs);
        glCompileShader(fs);
        glAttachShader(program, fs);

        glLinkProgram(program);
        glValidateProgram(program);

        this.vs_source = source_vs;
        this.fs_source = source_fs;
    }
    
    public Shaders(InputStream stream_vs, InputStream stream_fs, Pair<String, String>... defines) {
        this(readFileFromStream(stream_vs, defines), readFileFromStream(stream_fs, defines));
    }

    private static String readFile(File file, Pair<String, String>... defines){
        StringBuilder string = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            String prevLine = "";
            while((line = br.readLine()) != null){
                if(defines != null){
                    if((prevLine.contains("#version") || prevLine.contains("#extension")) && !line.contains("#")){
                        for(var p : defines){
                            string.append("\n").append("#define ").append(p.left).append(" ").append(p.right).append("\n");
                        }
                    }
                }
                if(line.contains("#include")){
                    line = line.replaceAll("\\s", "");
                    line = line.replace("#include", "");
                    string.append(readFileFromStream(Shaders.class.getResourceAsStream(line)));
                    continue;
                }
                string.append(line);
                string.append("\n");
                prevLine = line;
            }
        }catch(IOException e){
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return string.toString();
    }
    
    private static String readFileFromStream(InputStream stream, Pair<String, String>... defines) {
        StringBuilder string = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(stream))){
            String line;
            String prevLine = "";
            while((line = br.readLine()) != null){
                if(defines != null){
                    if((prevLine.contains("#version") || prevLine.contains("#extension")) && !line.contains("#")){
                        for(var p : defines){
                            string.append("\n").append("#define ").append(p.left).append(" ").append(p.right).append("\n");
                        }
                    }
                }
                if(line.contains("#include")){
                    line = line.replaceAll("\\s", "");
                    line = line.replace("#include", "");
                    string.append(readFileFromStream(Shaders.class.getResourceAsStream(line)));
                    continue;
                }
                string.append(line);
                string.append("\n");
                prevLine = line;
            }
        }catch(IOException e){
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return string.toString();
    }

    public void bind(){
        glUseProgram(program);
    }

    public void unbind(){
        glUseProgram(0);
    }

    public int getProgram(){
        return program;
    }

    public void setUniform(String uniformName, Matrix4f value){
    	int location = glGetUniformLocation(program, uniformName);
        if(location < 0){
            LOGGER.log(Level.SEVERE, errorMessage(uniformName, location));
            System.exit(1);
        }
        try(MemoryStack stack = MemoryStack.stackPush()){
            FloatBuffer buffer = stack.mallocFloat(4*4);
            value.get(buffer);
            glUniformMatrix4fv(location, false, buffer);
        }
    }

    public void setUniform(String uniformName, int value){
    	int location = glGetUniformLocation(program, uniformName);
        if(location < 0){
        	LOGGER.log(Level.SEVERE, errorMessage(uniformName, location));
            System.exit(1);
        }
        glUniform1i(location, value);
    }
    
    public void setUniform(String uniformName, float value){
    	int location = glGetUniformLocation(program, uniformName);
        if(location < 0){
        	LOGGER.log(Level.SEVERE, errorMessage(uniformName, location));
            System.exit(1);
        }
        glUniform1f(location, value);
    }
    
    public void setUniform(String uniformName, Vector4f value){
    	int location = glGetUniformLocation(program, uniformName);
        if(location < 0){
        	LOGGER.log(Level.SEVERE, errorMessage(uniformName, location));
            System.exit(1);
        }
        try(MemoryStack stack = MemoryStack.stackPush()){
            FloatBuffer buffer = stack.mallocFloat(4);
            value.get(buffer);
            glUniform4fv(location, buffer);
        }
    }
    
    public void setUniform(String uniformName, Vector3f value){
    	int location = glGetUniformLocation(program, uniformName);
        if(location < 0){
        	LOGGER.log(Level.SEVERE, errorMessage(uniformName, location));
            System.exit(1);
        }
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        value.get(buffer);
        glUniform3fv(location, buffer);
    }
    
    public void setUniform(String uniformName, Vector2f value){
    	int location = glGetUniformLocation(program, uniformName);
        if(location < 0){
        	LOGGER.log(Level.SEVERE, errorMessage(uniformName, location));
            System.exit(1);
        }
        try(MemoryStack stack = MemoryStack.stackPush()){
            FloatBuffer buffer = stack.mallocFloat(4);
            value.get(buffer);
            glUniform2fv(location, buffer);
        }
    }
    
    private final String errorMessage(String uniformName, int location) {
    	return "Could not set uniform " + uniformName + " because location is " + location;
    }

    public static Shaders getUberShader(String shader_vs_dir, String shader_fs_dir, Pair<String, String>... defines){
        String name_vs = shader_vs_dir.substring(shader_vs_dir.lastIndexOf("/")+1);
        String name_fs = shader_fs_dir.substring(shader_fs_dir.lastIndexOf("/")+1);
        Shaders s;
        final StringBuilder builder = new StringBuilder();
        builder.append(name_vs).append(name_fs).append(definesToString(defines));
        if((s = InMemoryCache.INSTANCE.get(builder.toString())) == null){
            s = new Shaders(Shaders.class.getResourceAsStream(shader_vs_dir), Shaders.class.getResourceAsStream(shader_fs_dir), defines);
            InMemoryCache.INSTANCE.add(builder.toString(), s, 1000000);
        }
        return s;
    }

    private static String definesToString(Pair<String, String>... defines){
        final StringBuilder builder = new StringBuilder();
        for(var d:defines){
            builder.append(d.left).append("_");
        }
        return builder.toString();
    }
}
