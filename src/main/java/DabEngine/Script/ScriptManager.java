package DabEngine.Script;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.*;

public class ScriptManager {

	private static Globals globals = JsePlatform.standardGlobals();

	public static void run(String script){
		globals.loadfile(script).call();
	}

	public static void bind(String name, Object value){
		globals.set(LuaValue.valueOf(name), CoerceJavaToLua.coerce(value));
	}

	public static LuaValue getBindings(String name){
		return globals.get(LuaValue.valueOf(name));
	}

	public static Varargs invokeMethod(String name, Object... args){
		if(args != null && args.length > 0) {
			LuaValue[] values = new LuaValue[args.length];
			for(int i = 0; i < args.length; i++){
				values[i] = CoerceJavaToLua.coerce(args[i]);
			}
			return globals.get(name).invoke(LuaValue.varargsOf(values));
		}else{
			return globals.get(name).call();
		}
	}
}
