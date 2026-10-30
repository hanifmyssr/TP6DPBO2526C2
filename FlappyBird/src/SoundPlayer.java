import javax.sound.sampled.*;
import java.net.URL;

public class SoundPlayer {
    private Clip clip;

    public SoundPlayer(String path) {
        try {
            URL url = getClass().getResource(path);
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            System.out.println("Gagal load audio: " + path);
        }
    }

    public void play() {
        if (clip == null) return;
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }
}
