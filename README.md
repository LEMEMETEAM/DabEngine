<h1>DabEngine</h1>
<p>This is just a simple 2D and 3D engine. It still has small bugs here and there but those will be ironed out soon.</p>

<h2>Structure</h2>
`dab` contains the engine code.</br>
`/Animation Test` and `\Testing` contain some small tests that I made (you can try them out for yourselves).</br>
`/res` contains textures and sprites.</br>

<h3>Usage</h3>
<p>To use in your own projects just take the `/src` folder and import it into your IDE of choice.</p>
<p>To use the engine, you first have to create your main class then extend the `Engine` class. You then just have to create an object of your main class in psvm and call `start()`. For Eeample:</br>
```java
public class Main extends Engine{
  public static void main(String[] args){
    new Main().start();
  }
}```
When you run the code, if done correctly, you should just get a black screen.
<p>Also all OpenGL code and anything related to OpenGL should be called in the `initWindow()` method since it creates all OpenGL contexts in that same super method.

<h4>Features That Need To Be Added</h4>
<li>Fully working TextureSheet & Animations</li>
<li>Better optimizations (don't know if it's just my shit laptop)</li>
<li>More HUD methods (such as draw image or shape, not just text (I only used the HUD for debugging))</li>
<li>Learn more about shaders so they can be implemented better (you have to create all 5 default uniform variables manually)</li>
<li>Window title and size changing (will be one of the first added (I am also not 100% sure if the resize callback even works so I will fix that as well))</li>
