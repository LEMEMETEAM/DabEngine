package Script;

import org.python.core.Py;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.core.PySystemState;

public class ScriptRunner<T> {
	
	private PyObject module;
	private Class<T> type;
	
	public ScriptRunner(PySystemState state, Class<T> returnType, String moduleName) {
		this.type = returnType;
		PyObject importer = state.getBuiltins().__getitem__(Py.newString("__import__"));
		module = importer.__call__(Py.newString(moduleName));
	}
	
	@SuppressWarnings("unchecked")
	public T runFunction(String funcName, Object... args) {
		PyObject convertedArgs[] = new PyObject[args.length];
	     for (int i = 0; i < args.length; i++) {
	         convertedArgs[i] = Py.java2py(args[i]);
	     }

	     return (T) module.invoke(funcName, convertedArgs).__tojava__(type);
	}
	
	@SuppressWarnings("unchecked")
	public T runFunctionFromClass(String className, String funcName, Object... args) {
		PyObject convertedArgs[] = new PyObject[args.length];
	     for (int i = 0; i < args.length; i++) {
	         convertedArgs[i] = Py.java2py(args[i]);
	     }
	     
	     PyObject clz = module.__getattr__(className);

	     return (T) clz.invoke(funcName, convertedArgs).__tojava__(type);
	}
}
