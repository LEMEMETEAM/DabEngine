package DabEngine.Script;

import java.lang.reflect.InvocationTargetException;

public class ScriptRunner {
	
	/**
	 * Runs a scala scripts main method in code.
	 */
	public static void run(String classname, String... args) {
		try {
			Class.forName(classname).getMethod("main", null).invoke(null, new Object[] { args });
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
