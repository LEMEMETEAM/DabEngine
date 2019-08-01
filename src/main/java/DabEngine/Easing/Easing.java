package DabEngine.Easing;

/**
 * Provides easing functions that can be used to change a value over time.
 */
public enum Easing {

	LINEAR{
		@Override
		public float ease(float from, float to, float time, float duration) {
			return time * (to - from) / duration + from;
		}
	};
	
	/**
	 * Has to be overrided by each function
	 */
	public float ease(float from, float to, float time, float duration){ 
		return 0;
	}
}	
