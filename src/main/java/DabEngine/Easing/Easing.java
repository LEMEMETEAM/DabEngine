package DabEngine.Easing;

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
