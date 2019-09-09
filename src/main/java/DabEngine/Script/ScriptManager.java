package DabEngine.Script;

import java.lang.reflect.InvocationTargetException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.naming.Binding;
import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import DabEngine.Core.IDisposable;

public class ScriptManager {

	private static ScriptEngine eng;
	public static Bindings binds;

    static {
		eng = new ScriptEngineManager().getEngineByExtension("kts");
		eng.setBindings(eng.createBindings(), ScriptContext.ENGINE_SCOPE);
    }

    public static void run(String file, String... args){
		try {
			eng.eval(new FileReader(file));
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void bind(String val_name, Object value){

		eng.getBindings(ScriptContext.ENGINE_SCOPE).put(val_name, value);
		
	}

	public static void unbind(String val_name){

		eng.getBindings(ScriptContext.ENGINE_SCOPE).remove(val_name
		);
		
	}

    public static <T> T invokeFunction(Class<T> type, String function, Object... args){
		
		Invocable i = (Invocable)eng;

		try {
			return (T) i.invokeFunction(function, args);
		} catch (NoSuchMethodException | ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public static <T> T invokeMethod(Class<T> type, String obj, String method, Object... args){

		Invocable i = (Invocable)eng;

		try {
			return (T) i.invokeMethod(eng.get(obj), method, args);
		} catch (NoSuchMethodException | ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Object get(String val){
		return eng.get(val);
	}

}
