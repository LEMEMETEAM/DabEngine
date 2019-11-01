package DabEngine.Script;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.jruby.Ruby;
import org.jruby.RubyRuntimeAdapter;
import org.jruby.internal.runtime.GlobalVariable.Scope;
import org.jruby.javasupport.Java;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.javasupport.JavaObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.javasupport.JavaEmbedUtils.EvalUnit;
import org.jruby.runtime.GlobalVariable;
import org.jruby.runtime.builtin.IRubyObject;

import DabEngine.Core.IDisposable;
import DabEngine.Resources.Resource;

public enum ScriptManager implements IDisposable
{

	INSTANCE;

	private Ruby runtime;
	private RubyRuntimeAdapter evaler;
	public static final int HOOK_COUNT = 1000;
	
	/*
	disallowedInRubyOpts(argument);
	Options.DEBUG_FULLTRACE.force("true");
	RubyInstanceConfig.FULL_TRACE_ENABLED = true;
	config.setDebuggingFrozenStringLiteral(true);
	*/
	
	{


		runtime = Ruby.getGlobalRuntime();
		evaler = JavaEmbedUtils.newRuntimeAdapter();

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
		
		return runtime.getTopSelf().callMethod(runtime.getCurrentContext(), name, rubyArgs);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		JavaEmbedUtils.terminate(runtime);
	}

	public void execFile(String filename) {
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
			return;
		}

		evaler.eval(runtime, source.toString());
	}
	
}
