package DabEngine.Graphics.OpenGL;

import static org.lwjgl.opengl.GL33.*;

import DabEngine.Utils.Procedure;

public class Blending {
    public static final Blending ADD = new Blending(() -> {
        glBlendEquation(GL_FUNC_ADD);
        glBlendFuncSeparate(GL_ONE, GL_ONE, GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    });
    public static final Blending SUB = new Blending(() -> {
        glBlendEquation(GL_FUNC_SUBTRACT);
        glBlendFuncSeparate(GL_ONE, GL_ONE, GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    });
    public static final Blending MUL = new Blending(() -> {
        glBlendEquation(GL_FUNC_ADD);
        glBlendFuncSeparate(GL_DST_COLOR, GL_ZERO, GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    });
    public static final Blending MIX = new Blending(() ->  {
        glBlendEquation(GL_FUNC_ADD);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    });

    private Procedure action;
    public Blending(Procedure action){
        this.action = action;
    }

    public void apply(){
        action.run();
    }
}