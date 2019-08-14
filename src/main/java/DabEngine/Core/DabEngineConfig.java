package DabEngine.Core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.glfw.GLFW.*;

public enum DabEngineConfig implements Command{
    /**
     * arg 1 = true/false
     */
    MULTISAMPLE(){
        @Override
        public void execute(Object... obj) {
            if(((String)obj[0]).equals("true")) glEnable(GL_MULTISAMPLE); else glDisable(GL_MULTISAMPLE);
        }
    },
    VSYNC(){
        @Override
        public void execute(Object... obj) {
            if(((String)obj[0]).equals("true")) glfwSwapInterval(1); else glfwSwapInterval(0);
        }
    },
    /**
     * arg 1 = width
     * arg 2 = app
     */
    RESOLUTION_W(){
        @Override
        public void execute(Object... obj) {
            int width = (int)obj[0];
            App app = (App)obj[1];

            app.WIDTH = width;
        }
    },
    /**
     * arg 1 = width
     * arg 2 = app
     */
    RESOLUTION_H(){
        @Override
        public void execute(Object... obj) {
            int height = (int)obj[0];
            App app = (App)obj[1];

            app.HEIGHT = height;
        }
    };

    DabEngineConfig(){};

    public void setValue(Object... args){
        ConfigProps.props.setProperty(this.name(), (String)args[0]);
        execute(args);
    }

    public String getValue(String key){
        return ConfigProps.props.getProperty(this.name());
    }

	@Override
	public void execute(Object... obj) {
		
	}
}

class ConfigProps{
    public static Properties props;

    static {
        props = new Properties();
        try {
            FileInputStream in = new FileInputStream("./config.ini");
            props.load(in);
            in.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void save(){
        try {
            FileOutputStream out = new FileOutputStream("./config.ini");
            props.store(out, null);
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private ConfigProps(){}


}