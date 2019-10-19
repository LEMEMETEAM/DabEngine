package DabEngine.Script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.jruby.Profile;
import org.jruby.Ruby;
import org.jruby.RubyInstanceConfig;
import org.jruby.RubyRuntimeAdapter;
import org.jruby.embed.EmbedEvalUnit;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;
import org.jruby.embed.internal.BiVariableMap;
import org.jruby.internal.runtime.GlobalVariable.Scope;
import org.jruby.javasupport.Java;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.javasupport.JavaObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.javasupport.JavaEmbedUtils.EvalUnit;
import org.jruby.runtime.EventHook;
import org.jruby.runtime.GlobalVariable;
import org.jruby.runtime.RubyEvent;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.util.cli.Options;

import DabEngine.Core.IDisposable;
import DabEngine.Utils.MultiFunc;
import DabEngine.Utils.Pair;
import DabEngine.Utils.Utils;

public enum ScriptManager implements IDisposable {

	INSTANCE;

	private Ruby runtime;
	private RubyRuntimeAdapter evaler;
	private Pair<String, EvalUnit> lastScript;
	private IRubyObject _loaded;
	public static final int HOOK_COUNT = 1000;
	
	/*
	disallowedInRubyOpts(argument);
	Options.DEBUG_FULLTRACE.force("true");
	RubyInstanceConfig.FULL_TRACE_ENABLED = true;
	config.setDebuggingFrozenStringLiteral(true);
	*/
	
	ScriptManager(){
		runtime = Ruby.getGlobalRuntime();
		evaler = JavaEmbedUtils.newRuntimeAdapter();
	}

	public void load(String script){
		if(lastScript == null || lastScript.left.equals(script)){
			lastScript = new Pair<>(script, evaler.parse(runtime, Thread.currentThread().getContextClassLoader().getResourceAsStream(script), script, 0));
		}
		_loaded = lastScript.right.run();
	}

	public void bind(String key, Object value){
		runtime.defineVariable(new GlobalVariable(runtime, "$"+key, JavaEmbedUtils.javaToRuby(runtime, value)), Scope.GLOBAL);
	}

	public void bindConstant(String key, Object value){
		runtime.defineReadonlyVariable("$"+key, JavaEmbedUtils.javaToRuby(runtime, value), Scope.GLOBAL);
	}

	public IRubyObject getBinding(String key){
		return runtime.getGlobalVariables().get("$"+key);
	}

	public IRubyObject invokeMethod(String name, Object... args){
		IRubyObject[] rubyArgs = JavaUtil.convertJavaArrayToRuby(runtime, args);

        // Create Ruby proxies for any input arguments that are not primitives.
        for (int i = 0; i < rubyArgs.length; i++) {
            IRubyObject obj = rubyArgs[i];

            if (obj instanceof JavaObject) rubyArgs[i] = Java.wrap(runtime, obj);
		}
		
		return _loaded.callMethod(runtime.getCurrentContext(), name, rubyArgs);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		JavaEmbedUtils.terminate(runtime);
	}
	
}
