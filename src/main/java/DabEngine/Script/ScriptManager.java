package DabEngine.Script;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;
import org.luaj.vm2.lib.jse.JsePlatform;

import DabEngine.Core.IDisposable;
import DabEngine.Resources.Resource;

public enum ScriptManager
{

	INSTANCE;

	private Globals globals;
	private LuaValue chunk;
	
	{
		globals = JsePlatform.standardGlobals();
	}

	public void bind(String key, Object value){
		globals.set(LuaValue.valueOf(key), CoerceJavaToLua.coerce(value));
	}

	public LuaValue getBinding(String key){
		return globals.get(LuaValue.valueOf(key));
	}

	public Varargs invokeMethod(String name, Object... args){
		LuaValue[] lArgs = new LuaValue[args.length];
		for(int i = 0; i < lArgs.length; i++)
		{
			lArgs[i] = CoerceJavaToLua.coerce(args[i]);
		}
		return globals.get(LuaValue.valueOf(name)).checkfunction().invoke(lArgs);
	}

	public LuaValue execFile(String filename) {
		// TODO Auto-generated method stub
		StringBuilder source = new StringBuilder();
		try(BufferedReader b = new BufferedReader(new FileReader(filename)))
		{
			String s;
			while((s = b.readLine()) != null)
			{
				source.append(s).append("\n");
			}
			source.append("\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		chunk = globals.load(source.toString(), "main.lua");
		return chunk.call();
	}
	
}
