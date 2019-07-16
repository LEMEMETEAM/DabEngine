package DabEngine.System;

import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import DabEngine.Entities.Entity;
import DabEngine.Entities.EntityManager;
import DabEngine.Entities.Components.CScript;
import DabEngine.Graphics.Graphics;
import DabEngine.Script.ScriptRunner;


public class ScriptSystem extends ComponentSystem {
	
	private ScriptRunner runner = new ScriptRunner(new PySystemState());

	@Override
	public void update() {
		// TODO Auto-generated method stub
		for(Entity e : EntityManager.entitiesWithComponents(CScript.class)) {
			CScript s = e.getComponent(CScript.class);
			if(s.className == null && s.functionName == null){
				runner.runMain(s.moduleName);
				continue;
			}
			if(s.className == null){
				runner.runFunction(s.moduleName, s.functionName, s.type, s.args);
				continue;
			}
			if(s.functionName == null);
			else{
				runner.runFunctionFromClass(s.moduleName, s.className, s.functionName, s.type, s.args);
				continue;
			}
		}
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
}
