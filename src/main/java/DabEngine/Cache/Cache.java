package DabEngine.Cache;

public interface Cache {
	
	public <T> void add(String filename, T objectToCache, long lifeTime);
	public void remove(String filename);
	public long size();
	public <T> T get(String filename);
	public void clear();
	public void cleanUp();
}
