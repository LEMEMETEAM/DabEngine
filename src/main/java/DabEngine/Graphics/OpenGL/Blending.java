package DabEngine.Graphics.OpenGL;

import static org.lwjgl.opengl.GL33.*;

import DabEngine.Utils.Procedure;

public enum Blending {
    ADD(() -> {
        glBlendEquation(GL_FUNC_ADD);
        glBlendFuncSeparate(GL_ONE, GL_ONE, GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }),
    SUB(() -> {
        glBlendEquation(GL_FUNC_SUBTRACT);
        glBlendFuncSeparate(GL_ONE, GL_ONE, GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }),
    MUL(() -> {
        glBlendEquation(GL_FUNC_ADD);
        glBlendFuncSeparate(GL_DST_COLOR, GL_ZERO, GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }),
    MIX(() ->  {
        glBlendEquation(GL_FUNC_ADD);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    });

    private Procedure action;
    Blending(Procedure action){
        this.action = action;
    }

    public void apply(){
        action.run();
    }
}