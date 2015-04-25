/**
 * Created by Victor Ignatenkov on 4/5/15.
 */

package a4.app.utilities;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;


/**
 * Sound - allows to create a sound object with an
 * actual sound located in the resource folder.
 */
public class Sound {

    AudioClip myClip;

    public Sound(String fileName) {

        String pathToResources = Services.getPathToSoundResources();

        try {
            File file = new File(pathToResources + fileName);
            if (file.exists()) {
                myClip = Applet.newAudioClip(file.toURI().toURL());
            } else {
                throw new RuntimeException("Sound: file not found: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Sound: malformed URI: " + e);
        }
    }

    /**
     * Play the sound
     */
    public void play(){
        myClip.play();
    }

    /**
     * Loop the sound
     */
    public void loop(){
        myClip.loop();
    }

    /**
     * Stop the sound
     */
    public void stop(){
        myClip.stop();
    }

}
