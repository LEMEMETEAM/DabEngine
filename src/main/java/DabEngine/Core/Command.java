package DabEngine.Core;

/**
 * Functional Interface that takes a variable number of args
 */
public interface Command {
	
	public void execute(Object... obj);
}
