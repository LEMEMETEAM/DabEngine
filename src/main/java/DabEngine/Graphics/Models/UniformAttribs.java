package DabEngine.Graphics.Models;

public class UniformAttribs {
    public final int pos;
    public final String name;
    public final int numComponents;
    
    public UniformAttribs(int pos, String name, int numComponents) {
        this.pos = pos;
        this.name = name;
        this.numComponents = numComponents;
    }
}