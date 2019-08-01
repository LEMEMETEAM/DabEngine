package DabEngine.Cache;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum InMemoryCache implements Cache {
	
	INSTANCE;

	public long CACHE_CLEANUP_SLEEP_TIME = 5;
	@SuppressWarnings("rawtypes")
	private ConcurrentHashMap<String, SoftReference<CachedObject>> cache = new ConcurrentHashMap<>();
	private final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	/**
	 * Singleton class that caches objects that are used multiple times.
	 * Runs on a separate thread.
	 */
	InMemoryCache() {
		Thread cleanUpThread = new Thread(() -> {
			while(true) {
				try {
					Thread.sleep(CACHE_CLEANUP_SLEEP_TIME * 1000L);
					cleanUp();
				} catch (InterruptedException e) {
					LOGGER.log(Level.WARNING, "Thread Error", e);
					Thread.currentThread().interrupt();
				}
			}
		});
		
		cleanUpThread.setDaemon(true);
		cleanUpThread.start();
	}

	/**
	 * Adds an object to the cache.
	 * @param refName reference name of the object to cache
	 * @param objectToCache object to cache
	 * @param lifeTime life time of cache object in milliseconds
	 */
	@Override
	public <T> void add(String refName, T objectToCache, long lifeTime) {
		if(refName.isEmpty() || refName == null) {
			return;
		}
		if(objectToCache == null) {
			cache.remove(refName);
		} else {
			lifeTime += System.currentTimeMillis();
			cache.put(refName, new SoftReference<>(new CachedObject<T>(objectToCache, lifeTime)));
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

	/**
	 * cleans up any expired cache objects
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void cleanUp() {
		Iterator<Entry<String, SoftReference<CachedObject>>> it = cache.entrySet().iterator();
		while(it.hasNext()) {
			if(it.next().getValue().get().isExpired()) {
				it.remove();
			}
		}
	}
}
