package DabEngine.Core;

import org.lwjgl.glfw.GLFW;

import DabEngine.Input.KeyEvent;
import DabEngine.Input.KeyboardEventListener;
import DabEngine.Observer.Event;

public class GameLoadingTest {

    public static void main(String[] args) {
        AppConfig config = new AppConfig();
        config.title = "test";
        config.width = 800;
        config.height = 600;
        App app = new App(new GameAdapter(), config);
        app.run();
    }
}

class GameAdapter extends AppAdapter {

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public void render() {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        System.out.println("work");
        app.getInput().getKeyboard().addObserver(new KeyboardEventListener() {
            @Override
            public void onKeyDown(KeyEvent e) {
                // TODO Auto-generated method stub
                System.out.println(GLFW.glfwGetKeyName(e.getKey(), e.getScancode()));
            }

            @Override
            public void onNotify(Event e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onKeyUp(KeyEvent e) {
                // TODO Auto-generated method stub

            }
        });
        //GameLoadingTest.app.getGProcessor().setFullscreenMode(GameLoadingTest.app.getGProcessor().getDisplayMode(GameLoadingTest.app.getGProcessor().getPrimaryMonitor()));
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        System.out.println("hmmm");
    }

}