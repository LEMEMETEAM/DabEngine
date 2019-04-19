package Cache;

public class CachedObject<T> {
	
	private T value;
	private long lifeTime;
	
	public CachedObject(T value, long lifeTime) {
		this.value = value;
		this.lifeTime = lifeTime;
	}
	
	public boolean isExpired() {
		return System.currentTimeMillis() > lifeTime;
	}
	
	public T getValue() {
		return value;
	}
}
