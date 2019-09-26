package DabEngine.Cache;

public class CachedObject<T> {
	
	private T value;
	
	/**
	 * Wrapper for an object that is put inside {@link InMemoryCache}
	 * @param value the object to put in cache
	 */
	public CachedObject(T value) {
		this.value = value;
	}
	
	/**
	 * Get the object
	 * @return object
	 */
	public T getValue() {
		return value;
	}
}
