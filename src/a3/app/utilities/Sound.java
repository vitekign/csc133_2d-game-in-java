package a3.app.utilities;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;

/**
 * Created by Victor Ignatenkov on 4/5/15.
 */
public class Sound {

    AudioClip myClip;

    public Sound(String fileName) {

        String slash = File.separator;

        //TODO move resources one level up and refactor all corresponding code

        String pathToResources = "."  + slash + "src" + slash +"resources" + slash + "sounds" + slash;

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

    public void play(){
        myClip.play();
    }

}
