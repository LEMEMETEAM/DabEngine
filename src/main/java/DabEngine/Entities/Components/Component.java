package DabEngine.Entities.Components;

import java.io.Serializable;
import java.util.Objects;

public abstract class Component implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1910311515690798240L;

	@Override
	public int hashCode() {
		return 31 * Objects.hashCode(serialVersionUID) + super.hashCode();
	}
}
