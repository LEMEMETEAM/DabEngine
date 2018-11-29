package Core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import Graphics.Window;
import Input.*;
import org.joml.Matrix4f;

public abstract class Engine implements Runnable {

    private int width, height;
    private String title;
    private float FOV = 90;
    private final float Z_NEAR = 0.01f;
    private final float Z_FAR = 1000.f;
    private static Window mainWindow = null;
    private Thread t;
    private Matrix4f projectionMatrix;
    private float aspectRatio;
    private int finalFrames = 0;
    private int finalUpdates = 0;
    private String projectionMatrixType;
    private static double delta = 0.0;

    public synchronized void start() {
        t = new Thread(this, "gameThread");
        try {
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        double ns = 1000000000.0 / 60.0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;

        initWindow(width, height, title);
        while (!glfwWindowShouldClose(mainWindow.getWin())) {
            if(mainWindow.isResized()){
                glViewport(0, 0, mainWindow.getWidth(), mainWindow.getHeight());
                aspectRatio = (float) mainWindow.getWidth() / (float) mainWindow.getHeight();
            }
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1.0) {
                update();
                updates++;
                delta--;
            }
            render();
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                finalFrames = frames;
                finalUpdates = updates;
                System.out.println(updates + " ups, " + frames + " fps");
                updates = 0;
                frames = 0;
            }
        }
        glfwDestroyWindow(mainWindow.getWin());
        glfwTerminate();
    }

    public void initWindow(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;

        if (!glfwInit()) {
            System.err.println("Window not initialised");
            System.exit(1);
        }

        mainWindow = new Window(width, height, title);
        mainWindow.showWindow();

        glViewport(0, 0, mainWindow.getWidth(), mainWindow.getHeight());

        glfwSetKeyCallback(mainWindow.getWin(), new Input());
        glfwSetCursorPosCallback(mainWindow.getWin(), new Mouse());
        glfwSwapInterval(1);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_STENCIL_TEST);

        aspectRatio = (float) mainWindow.getWidth() / (float) mainWindow.getHeight();
    }

    public void update() {
        onUpdate();
        glfwPollEvents();
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

        onRender();

        glfwSwapBuffers(mainWindow.getWin());
    }

    public abstract void onRender();
    public abstract void onUpdate();

    public static Window getMainWindow() {
        return mainWindow;
    }

    public Matrix4f getProjectionMatrix() {
        if(mainWindow.isResized()){
            if(projectionMatrixType.equals("2D")){
                projectionMatrix = new Matrix4f().ortho(-10.0f * aspectRatio, 10.0f * aspectRatio, -10.0f, 10.0f, -1.0f, 1.0f);
            }
            else if(projectionMatrixType.equals("3D")){
                projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(FOV), aspectRatio, Z_NEAR, Z_FAR);
            }
        }
        return projectionMatrix;
    }

    public void setProjection(String type){
        switch(type){
            case "2D":
                projectionMatrixType = "2D";
                projectionMatrix = new Matrix4f().ortho(-10.0f * aspectRatio, 10.0f * aspectRatio, -10.0f, 10.0f, -1.0f, 1.0f);
                break;
            case "3D":
                projectionMatrixType = "3D";
                projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(FOV), aspectRatio, Z_NEAR, Z_FAR);
                break;
        }
    }

    public void setFOV(float FOV) {
        this.FOV = FOV;
    }

    public float getFOV() {
        return FOV;
    }

    public int getFrames() {
        return finalFrames;
    }

    public int getUpdates() {
        return finalUpdates;
    }

    public static double getDelta() {
        return delta;
    }
}