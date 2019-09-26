package DabEngine.Cache;

import DabEngine.Core.IDisposable;

public class CachedObject<T> implements IDisposable {
	
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

        @Override
        public void dispose(){
                if(value instanceof Disposable){
                       ((IDisposable)value).dispose();
                }
        }
}
