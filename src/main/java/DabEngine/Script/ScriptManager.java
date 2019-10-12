package DabEngine.Script;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.jruby.embed.EmbedEvalUnit;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;
import org.jruby.javasupport.JavaEmbedUtils;

import DabEngine.Utils.MultiFunc;
import DabEngine.Utils.Utils;

public class ScriptManager {

	private static ScriptingContainer container = new ScriptingContainer();
	private static EmbedEvalUnit parsed;
	private static String _last;
	private static Object scriptObj;

	public static void run(String script){
		if(_last != script){
			parsed = container.parse(PathType.CLASSPATH, script);
			_last = script;
		}

		scriptObj = JavaEmbedUtils.rubyToJava(parsed.run());
	}

	public static void bind(String name, Object value){
		container.put(scriptObj, name, value);
	}

	public static <T> T invokeFunction(String name, Class<T> type, Object... args){
		return container.callMethod(scriptObj, name, args, type);
	}

	public static Object invokeFunction(String name, Object... args){
		return container.callMethod(scriptObj, name, args);
	}

	public static void get(String name){
		container.get(name);
	}
}
