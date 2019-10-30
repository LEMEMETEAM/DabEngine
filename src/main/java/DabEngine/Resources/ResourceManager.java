package DabEngine.Resources;

import DabEngine.Cache.InMemoryCache;
import DabEngine.Resources.Audio.Audio;
import DabEngine.Resources.Audio.Music;
import DabEngine.Resources.Audio.Sample;
import DabEngine.Resources.Font.Font;
import DabEngine.Resources.Script.Script;
import DabEngine.Resources.Shaders.Shaders;
import DabEngine.Resources.Textures.Texture;

public enum ResourceManager {

    INSTANCE;

    private InMemoryCache cache = new InMemoryCache.Builder().initialSize(16).maxSize(64).build();

    public static Texture defaultTexture = new Texture(4, 4, true, false);
    private static final String vs_source = "#version 330 core\n\n"//
    + "layout (location = 0) in vec3 position;\n"//
    + "layout (location = 1) in vec4 color;\n"//
    + "layout (location = 2) in vec2 uvs;\n"//
    + "layout (location = 3) in vec3 normals;\n\n"//
    + "out vec3 outPosition;\n"//
    + "out vec4 outColor;\n"//
    + "out vec2 outUV;\n"//
    + "out vec3 outNormals;\n\n"//
    + "layout (std140) uniform globals {\n"//
    + "     mat4 matrix;\n"//
    + "};\n\n"//
    + "void main(){\n"//
    + "     gl_Position = globals.matrix * vec4(position, 1.0);\n"//
    + "     outPosition = position;\n"//
    + "     outColor = color;\n"//
    + "     outUV = uvs;\n"//
    + "     outNormals = normals;\n"//
    + "}\n";

    private static final String fs_source = "#version 330 core\n\n"//
    + "in vec2 outUV;\n"//
    + "in vec4 outColor;\n"//
    + "out vec4 finalColor;\n\n"//
    + "uniform sampler2D texture0;\n\n"//
    + "void main(){\n"//
    + "     vec4 col = texture(texture0, outUV);\n"//
    + "     if(col == vec4(0)){\n"//
    + "         finalColor = outColor;\n"//
    + "     } else {\n"//
    + "         finalColor = col * outColor;\n"//
    + "     }\n"//
    + "     if(finalColor.a < 0.1){\n"//
    + "         discard;\n"//
    + "     }\n"//
    + "}\n";
    public static Shaders defaultShaders = new Shaders(vs_source, fs_source, true);
    private static boolean ready = false;

    public static void init()
    {
        defaultTexture.load();
        defaultShaders.load();
        ready = true;
    }

    private <T extends Resource> T get(String name)
    {
        ResourceHandle<T> o = cache.get(name);
        
        return o != null ? o.getResource() : null;
    }

    public Texture getTexture(String name, boolean mipmap, boolean hdr)
    {
        if(!ready)
        {
            //LOG
            throw new IllegalStateException("Init ResourceManager first");
        }

        Texture tex = get(name);
        if(tex == null)
        {
            Texture newTex = new Texture(name, mipmap, hdr);
            newTex.load();
            if(!newTex.ready)
            {
                return defaultTexture;
            }
            cache.add(name, new ResourceHandle<Texture>(newTex));
            return newTex;
        }
        return tex;
    }

    public Shaders getShader(String name_vs, String name_fs)
    {
        if(!ready)
        {
            //LOG
            throw new IllegalStateException("Init ResourceManager first");
        }

        String join = name_vs+"|"+name_fs;

        Shaders s = get(join);
        if(s == null)
        {
            Shaders newS;
            newS = new Shaders(name_vs, name_fs, false);
            newS.load();
            if(!newS.ready)
            {
                return defaultShaders;
            }
            
            cache.add(join, new ResourceHandle<Shaders>(newS));
            return newS;
        }
        return s;
    }   

    public Shaders getShaderFragment(String name)
    {
        if(!ready)
        {
            //LOG
            throw new IllegalStateException("Init ResourceManager first");
        }

        Shaders s = get(name);
        if(s == null)
        {
            Shaders newS;
            newS = new Shaders(vs_source, name, true);
            newS.load();
            if(!newS.ready)
            {
                return defaultShaders;
            }
            
            cache.add(name, new ResourceHandle<Shaders>(newS));
            return newS;
        }
        return s;        
    }

    public Shaders getShaderVertex(String name)
    {
        if(!ready)
        {
            //LOG
            throw new IllegalStateException("Init ResourceManager first");
        }

        Shaders s = get(name);
        if(s == null)
        {
            Shaders newS;
            newS = new Shaders(vs_source, name, true);
            newS.load();
            if(!newS.ready)
            {
                return defaultShaders;
            }
            
            cache.add(name, new ResourceHandle<Shaders>(newS));
            return newS;
        }
        return s;
    }

    public Shaders getShaderFromSource(String name, String... sources)
    {
        if(!ready)
        {
            //LOG
            throw new IllegalStateException("Init ResourceManager first");
        }

        Shaders s = get(name);
        if(s == null)
        {
            Shaders newS;
            newS = new Shaders(sources[0], sources[1], true);
            newS.load();
            if(!newS.ready)
            {
                return defaultShaders;
            }
            
            cache.add(name, new ResourceHandle<Shaders>(newS));
            return newS;
        }
        return s;
    }

    public Audio getAudio(String name, boolean stream, boolean looped)
    {
        if(!ready)
        {
            //LOG
            throw new IllegalStateException("Init ResourceManager first");
        }

        Audio audio = get(name);
        if(audio == null)
        {
            Audio newAudio;
            newAudio = stream ? new Music(name, looped) : new Sample(name, looped);
            newAudio.load();    

            if(!newAudio.ready)
            {
                return null;
            }

            cache.add(name, newAudio);
            return newAudio;

        }

        return audio;
    }

    public Script getScript(String name)
    {
        if(!ready)
        {
            //LOG
            throw new IllegalStateException("Init ResourceManager first");
        }

        Script s = get(name);
        if(s == null)
        {
            Script newScript = new Script(name);
            newScript.load();

            if(!newScript.ready)
            {
                return null;
            }

            cache.add(name, newScript);
            return newScript;
        }

        return s;
    }

    public Font getFont(String name, float size, int oversampling)
    {
        if(!ready)
        {
            //LOG
            throw new IllegalStateException("Init ResourceManager first");
        }

        Font f = get(name);

        if(f == null)
        {
            Font newFont = new Font(name, size, oversampling);
            newFont.load();

            if(!newFont.ready)
            {
                //TODO: add system fonts
                return null;
            }

            cache.add(name, newFont);
            return newFont;
        }

        return f;
    }
}