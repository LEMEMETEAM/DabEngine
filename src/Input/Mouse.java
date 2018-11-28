package Input;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import static org.lwjgl.glfw.GLFW.*;

public class Mouse extends GLFWCursorPosCallback {

    public static double dx;
    public static double dy;

    private double lastposx;
    private double lastposy;

    private static boolean moving = false;
    @Override
    public void invoke(long window, double xpos, double ypos) {

        moving = true;

        dx = xpos - lastposx;
        dy = ypos - lastposy;

        lastposx = xpos;
        lastposy = ypos;

    }

    public static boolean isMoving() {
        return moving;
    }

    public static void setMoving(boolean b) {
        moving = b;
    }
}
