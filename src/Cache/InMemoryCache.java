package Cache;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCache implements Cache {
	
	private static InMemoryCache INSTANCE;
	public int CACHE_CLEANUP_SLEEP_TIME = 5;
	@SuppressWarnings("rawtypes")
	private ConcurrentHashMap<String, SoftReference<CachedObject>> cache = new ConcurrentHashMap<>();
	
	public InMemoryCache() {
		Thread cleanUpThread = new Thread(() -> {
			while(true) {
				try {
					Thread.sleep(CACHE_CLEANUP_SLEEP_TIME * 1000);
					cleanUp();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		cleanUpThread.setDaemon(true);
		cleanUpThread.start();
	}
	
	public static InMemoryCache getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new InMemoryCache();
		}
		return INSTANCE;
	}

	@Override
	public <T> void add(String filename, T objectToCache, long lifeTime) {
		// TODO Auto-generated method stub
		if(filename.isEmpty() || filename == null) {
			return;
		}
		if(objectToCache == null) {
			cache.remove(filename);
		} else {
			lifeTime += System.currentTimeMillis();
			cache.put(filename, new SoftReference<>(new CachedObject<T>(objectToCache, lifeTime)));
		}
	}

	@Override
	public void remove(String filename) {
		// TODO Auto-generated method stub
		cache.remove(filename);
	}

	@Override
	public long size() {
		// TODO Auto-generated method stub
		return cache.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String filename) {
		// TODO Auto-generated method stub
		CachedObject<T> t;
		try {
			t = cache.get(filename).get();
		} catch(NullPointerException ex) {
			return null;
		}
		return (T) t.getValue();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		cache.clear();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		Iterator<Entry<String, SoftReference<CachedObject>>> it = cache.entrySet().iterator();
		while(it.hasNext()) {
			if(it.next().getValue().get().isExpired()) {
				it.remove();
			}
		}
	}
}
