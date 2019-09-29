package DabEngine.Cache;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Map;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InMemoryCache {

	public static final class Builder {

		private int initialSize, maxSize;
		
		public Builder initialSize(int initialSize){
			this.initialSize = initialSize;
			return this;
		}

		public Builder maxSize(int maxSize){
			this.maxSize = maxSize;
			return this;
		}

		public InMemoryCache build(){
			return new InMemoryCache(initialSize, maxSize);
		}
	}

	private int initialSize, maxSize;

	private Map<String, SoftReference<CachedObject>> cache = Collections.synchronizedMap(new LinkedHashMap<>(initialSize, 1.0f, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, SoftReference<CachedObject>> eldest) {
				
                if(this.size() > maxSize){
					eldest.getValue().get().dispose();
					return true;
				} //must override it if used in a fixed cache
				return false;
            }

        });
	private final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	protected InMemoryCache(int initialSize, int maxSize){
		this.initialSize = initialSize;
		this.maxSize = maxSize;
	}

	/**
	 * Adds an object to the cache.
	 * @param refName reference name of the object to cache
	 * @param objectToCache object to cache
	 * @param lifeTime life time of cache object in milliseconds
	 */
	public <T> void add(String refName, T objectToCache) {
		if(refName.isEmpty() || refName == null) {
			return;
		}
		if(objectToCache == null) {
			cache.remove(refName);
		} else {
			cache.put(refName, new SoftReference<>(new CachedObject<T>(objectToCache)));
		}
	}
	
	/**
	 * Remove cache object at <code>refName</code>
	 */
	public void remove(String refName) {
		cache.remove(refName);
	}

	/**
	 * Get cache object
	 * @param refName reference name of object to get
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String refName) {
		CachedObject<T> t;
		try {
			t = cache.get(refName).get();
		} catch(NullPointerException ex) {
			return null;
		}
		return t.getValue();
	}

	/**
	 * clear the cache
	 */
	public void clear() {
		cache.clear();
	}
}
