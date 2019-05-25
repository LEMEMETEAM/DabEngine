package DabEngine.Easing;

public interface Easing {
	
	public float ease(float time, float from, float to, float duration);
	
	public static final Easing LINEAR = new Easing(){

		@Override
		public float ease(float time, float from, float to, float duration) {
			// TODO Auto-generated method stub
			return time * (to - from) / duration + from;
		}
		
	};
}	
