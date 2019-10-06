package DabEngine.Graphics.OpenGL.Shaders;

import static org.lwjgl.opengl.GL33.*;

import java.io.*;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import DabEngine.Cache.InMemoryCache;
import DabEngine.Core.IDisposable;
import DabEngine.Utils.Pair;

import DabEngine.Core.IDisposable;

/**
 * class that loads shaders, adds them to opengl and stores the id forthe program, vertex shader and fragment shader
 */
public class Shaders implements IDisposable {

    private int program;
    private int vs;
    private int fs;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public String vs_source, fs_source;
    private InMemoryCache uLocationCache = new InMemoryCache.Builder().initialSize(16).maxSize(32).build();
    private final FloatBuffer FLOATS = MemoryUtil.memAllocFloat(16);
    
    public Shaders(File filevs, File filefs, Pair<String, String>... defines) throws NullPointerException, IOException {
        this(read(filevs, defines), read(filefs, defines));
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

    private static String read(File file, Pair<String, String>... defines) throws NullPointerException, IOException {
        String shader_source = "";
        try{
            shader_source = readFile(new FileReader(file), defines);
        }catch (Exception io){
            shader_source = readFile(new InputStreamReader(Shaders.class.getResourceAsStream(file.getPath().replace("\\", "/"))), defines);
        }

        return shader_source;
    }

    private static String readFile(Reader file, Pair<String, String>... defines) throws IOException, NullPointerException {
        StringBuilder string = new StringBuilder();
        try(BufferedReader br = new BufferedReader(file)){
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
                    string.append(read(new File(line)));
                    continue;
                }
                string.append(line);
                string.append("\n");
                prevLine = line;
            }
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

    private int uniformLocation(String name){
        Integer loc = null;
        if((loc = uLocationCache.get(name)) == null){
            loc = glGetUniformLocation(program, name);
            if(loc == -1) {
                LOGGER.log(Level.SEVERE, errorMessage(name, loc));
                return -1;
            }
            else {
                uLocationCache.add(name, loc);
            }
        }
        return loc;
    }

    public void setUniform(String name, Matrix4fc value){
        FloatBuffer buffer = value.get(FLOATS);
        glUniformMatrix4fv(uniformLocation(name), false, buffer);
    }

    public void setUniform(String name, Vector2f value){
        glUniform2f(uniformLocation(name), value.x(), value.y());
    }

    public void setUniform(String name, Vector3f value){
        glUniform3f(uniformLocation(name), value.x(), value.y(), value.z());
    }

    public void setUniform(String name, Vector4f value){
        glUniform4f(uniformLocation(name), value.x(), value.y(), value.z(), value.w());
    }

    public void setUniform(String name, float value){
        glUniform1f(uniformLocation(name), value);
    }

    public void setUniform(String name, int value){
        glUniform1i(uniformLocation(name), value);
    }
    
    private final String errorMessage(String uniformName, int location) {
    	return "Could not set uniform " + uniformName + " because location is " + location;
    }


    public void dispose(){
        glDeleteProgram(program);
        glDeleteShader(vs);
        glDeleteShader(fs);
    }
}
