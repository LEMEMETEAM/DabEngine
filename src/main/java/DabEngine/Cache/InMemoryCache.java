package DabEngine.Cache;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Map;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum InMemoryCache {
	
	INSTANCE;

        private final int maxSize = 16;
	private Map<String, SoftReference<CachedObject>> cache = Collections.synchronizedMap(new LinkedHashMap<>(maxSize, 1.0f, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, SoftReference<CachedObject>> eldest) {
                return this.size() > maxSize; //must override it if used in a fixed cache
            }
            
            @Override
            public SoftReference<CachedObject> remove(Object key){
                var val = super.remove();
                if(val != null){
                    val.get().dispose();
                }
                return val;
            }
        });
	private final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	

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
	 * Check how many objects are in cache
	 */
	public long size() {
		return cache.size();
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
