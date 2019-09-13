package DabEngine.Audio;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import DabEngine.Utils.Timer;

public class MusicTest {

    @Test
    public void assertTimeIncrementsWhilePlaying(){
        try {
            Music m = new Music(new File("src/test/resources/audiocheck.wav"));
            long start_pos = m.getSamplePos();
            System.out.println(start_pos);
            m.play();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            long end_pos = m.getSamplePos();
            System.out.println(end_pos);
            assertTrue(start_pos < end_pos);
        } catch(Exception e){
            System.out.println("No audio available");
        }
    }
}
