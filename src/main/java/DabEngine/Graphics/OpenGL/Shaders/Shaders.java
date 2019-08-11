package DabEngine.Graphics.OpenGL.Shaders;

import static org.lwjgl.opengl.GL33.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

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
        if(glGetShaderi(vs, GL_COMPILE_STATUS) == 0){
        	LOGGER.log(Level.SEVERE, "Failed compiling vertex shader " + vs);
            LOGGER.log(Level.SEVERE, glGetShaderInfoLog(vs, glGetShaderi(vs, GL_INFO_LOG_LENGTH)));
            System.exit(0);
        }
        glAttachShader(program, vs);

        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs, source_fs);
        glCompileShader(fs);
        if(glGetShaderi(fs, GL_COMPILE_STATUS) == 0){
        	LOGGER.log(Level.SEVERE, "Failed compiling fragment shader " + fs);
            LOGGER.log(Level.SEVERE, glGetShaderInfoLog(fs, glGetShaderi(fs, GL_INFO_LOG_LENGTH)));
            System.exit(0);
        }
        glAttachShader(program, fs);

        glLinkProgram(program);
        if(glGetProgrami(program, GL_LINK_STATUS) == 0){
        	LOGGER.log(Level.SEVERE, "Failed linking shaders to program " + program);
            LOGGER.log(Level.SEVERE, glGetProgramInfoLog(program, glGetProgrami(program, GL_INFO_LOG_LENGTH)));
            //System.exit(0);
        }
        glValidateProgram(program);
        if(glGetProgrami(program, GL_VALIDATE_STATUS) == 0){
            LOGGER.log(Level.SEVERE, "PROGRAM VALIDATION FAILED FOR " + program);
            LOGGER.log(Level.SEVERE, glGetProgramInfoLog(program, glGetProgrami(program, GL_INFO_LOG_LENGTH)));
            
        }

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
                    if((prevLine.contains("#version") || prevLine.contains("#extension")) && !line.contains("#extension")){
                        for(var p : defines){
                            string.append("#define ").append(p.left).append(" ").append(p.right);
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
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4 * 4);
        value.get(buffer);
        glUniformMatrix4fv(location, false, buffer);
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
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        value.get(buffer);
        glUniform4fv(location, buffer);
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
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        value.get(buffer);
        glUniform2fv(location, buffer);
    }
    
    private final String errorMessage(String uniformName, int location) {
    	return "Could not set uniform " + uniformName + " because location is " + location;
    }
}
