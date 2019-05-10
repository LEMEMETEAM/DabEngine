package Script;

import org.python.core.Py;
import org.python.core.PyObject;
import org.python.core.PySystemState;

public class ScriptObjectBuilder<T> {
	
	private PyObject clazz;
	private Class<T> type;
	
	public ScriptObjectBuilder(PySystemState state, Class<T> type, String moduleName, String className) {
		this.type = type;
		PyObject importer = state.getBuiltins().__getitem__(Py.newString("__import__"));
		PyObject module = importer.__call__(Py.newString(moduleName));
		clazz = module.__getattr__(className);
	}
	
	@SuppressWarnings("unchecked")
	public T buildObject(Object... args) {
		PyObject convertedArgs[] = new PyObject[args.length];
		for (int i = 0; i < args.length; i++) {
			convertedArgs[i] = Py.java2py(args[i]);
		}
		
		return (T)clazz.__call__(convertedArgs, Py.NoKeywords).__tojava__(type);
	}
}
