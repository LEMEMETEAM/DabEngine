import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import DabEngine.Core.App;
import DabEngine.Core.Engine;

public class TestCaseEngine extends App {
    private static final Engine ENGINE = new Engine();

    {
        TITLE = "Test";
        WIDTH = 4;
        HEIGHT = 3;
        fullscreen = false;
        hints = new HashMap<>();
    }

    @Override
    public void render() {

    }

    @Override
    public void update() {

    }

    @Override
    public void init() {
        try{
            assertTrue(ENGINE.getMainWindow().isLoaded());
            ENGINE.end();
        }catch(AssertionError ae){
            ae.printStackTrace();
            ENGINE.end();
        }
    }

    public static void main(String[] args) {
        ENGINE.init(new TestCaseEngine());
        ENGINE.run();
    }
}