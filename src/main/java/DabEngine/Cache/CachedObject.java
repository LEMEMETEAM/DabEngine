package DabEngine.Cache;

public class CachedObject<T> {
	
	private T value;
	private long lifeTime;
	
	/**
	 * Wrapper for an object that is put inside {@link InMemoryCache}
	 * @param value the object to put in cache
	 * @param lifeTime the lifetime of the object
	 */
	public CachedObject(T value, long lifeTime) {
		this.value = value;
		this.lifeTime = lifeTime;
	}
	
	/**
	 * check to see if object has expired
	 * @return whether it has expired or not
	 */
	public boolean isExpired() {
		return System.currentTimeMillis() > lifeTime;
	}
	
	/**
	 * Get the object
	 * @return object
	 */
	public T getValue() {
		return value;
	}
}
