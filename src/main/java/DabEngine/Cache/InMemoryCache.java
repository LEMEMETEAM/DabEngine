package DabEngine.Cache;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum InMemoryCache {
	
	INSTANCE;

	private Map<String, SoftReference<CachedObject>> cache = Colections.synchronizedMap(new LinkedHashMap<>(16, 1.0f, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                return this.size() > maxSize; //must override it if used in a fixed cache
            }
            
            @Override
            public V remove(Object key){
                super.remove().dispose();
            }
        });
	private final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	

	/**
	 * Adds an object to the cache.
	 * @param refName reference name of the object to cache
	 * @param objectToCache object to cache
	 * @param lifeTime life time of cache object in milliseconds
	 */
	@Override
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
	@Override
	public void remove(String refName) {
		cache.remove(refName);
	}

	/**
	 * Check how many objects are in cache
	 */
	@Override
	public long size() {
		return cache.size();
	}

	/**
	 * Get cache object
	 * @param refName reference name of object to get
	 */
	@SuppressWarnings("unchecked")
	@Override
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
	@Override
	public void clear() {
		cache.clear();
	}
}
