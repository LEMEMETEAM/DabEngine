package DabEngine.Script;

import org.python.core.Py;
import org.python.core.PyObject;
import org.python.core.PySystemState;

public class ScriptRunner {
	
	PySystemState state;

	public ScriptRunner(PySystemState state) {
		this.state = state;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T runFunction(String moduleName, String funcName, Class<T> returnType, Object... args) {
		PyObject importer = state.getBuiltins().__getitem__(Py.newString("__import__"));
		PyObject module = importer.__call__(Py.newString(moduleName));

		PyObject convertedArgs[] = new PyObject[args.length];
	     for (int i = 0; i < args.length; i++) {
	         convertedArgs[i] = Py.java2py(args[i]);
	     }

	     return (T) module.invoke(funcName, convertedArgs).__tojava__(returnType);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T runFunctionFromClass(String moduleName, String className, String funcName, Class<T> returnType, Object... args) {
		PyObject importer = state.getBuiltins().__getitem__(Py.newString("__import__"));
		PyObject module = importer.__call__(Py.newString(moduleName));

		PyObject convertedArgs[] = new PyObject[args.length];
	     for (int i = 0; i < args.length; i++) {
	         convertedArgs[i] = Py.java2py(args[i]);
	     }
	     
	     PyObject clz = module.__getattr__(className);

	     return (T) clz.invoke(funcName, convertedArgs).__tojava__(returnType);
	}

	public void runMain(String moduleName) {
		PyObject importer = state.getBuiltins().__getitem__(Py.newString("__import__"));
		PyObject module = importer.__call__(Py.newString(moduleName));

		module.__call__();
	}
}
