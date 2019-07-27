package DabEngine.Utils;

import DabEngine.Easing.Easing;

public class AnimatedValue {

    private Easing easingFunc;
    private float from, to, duration, time, value;

    public AnimatedValue(float from, float to, float duration, Easing func){
        this.time = 0;
        this.from = from;
        this. to = to;
        this.duration = duration; 
        this.easingFunc = func;
    }

    public boolean update(float delta){
        float newtime = Utils.clamp(time + delta, 0, duration);
        if(time != newtime){
            this.time = newtime;
            value = easingFunc.ease(from, to, time, duration);
            return true;
        }
        return false;
    }

    public boolean isFinished(){
        return time == duration;
    }

    /**
     * @return the time
     */
    public float getTime() {
        return time;
    }

    /**
     * @return the value
     */
    public float getValue() {
        return value;
    }

}