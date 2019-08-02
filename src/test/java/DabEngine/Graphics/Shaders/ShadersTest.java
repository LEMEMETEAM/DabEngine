package DabEngine.Graphics.Shaders;

import java.io.File;

import org.junit.Test;

import DabEngine.Graphics.OpenGL.Shaders.Shaders;
import DabEngine.Utils.Pair;

public class ShadersTest {

    public void checkIfDefineInjectionWorks() {
        Shaders s = new Shaders(new File("src/test/resources/fbo.vs"), new File("src/test/resources/test.fs"), new Pair<>("BLUE", "1"));
        System.out.println(s.fs_source);
    }
}