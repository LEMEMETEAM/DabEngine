package DabEngine.Resources.Audio;

import javax.sound.sampled.AudioInputStream;

import DabEngine.Resources.Resource;

public abstract class Audio extends Resource
{

    protected AudioInputStream ais;
    protected boolean looped;

    public Audio(String filename, boolean looped)
    {
        super(filename);

        this.looped = looped;

    }

    public void loop(boolean b)
    {
        looped = b;
    }

    /**
     * @return the looped
     */
    public boolean isLooping() {
        return looped;
    }

    public abstract void play();
    public abstract void stop();
    public abstract void pause();

}