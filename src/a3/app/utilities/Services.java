package a3.app.utilities;

import a3.model.GameWorld;
import a3.model.Iterator;
import a3.objects.FuelCan;
import a3.objects.GameObject;
import a3.objects.Pylon;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.applet.AudioClip;
import java.awt.*;
import java.io.File;
import java.util.Random;
import java.util.Vector;

/**
 * Created by Victor Ignatenkov on 2/22/15.
 */
/**
 * Class Services provides various general-purpose
 * services, which don't belong to any specific class.
 *
 * Make constructor private so it behaves
 * and looks like a static class.
 */

public class Services {


    private static GameWorld gw;


    private Services(){
    }

    public static void supplyServicesWithGameWorld( GameWorld gameWorld)
    {
        gw  = gameWorld;
    }



    /**
     * @return
     * Returns a randomly generated java Color
     */

    public static Color generateRandomColor(){
        //(inclusive)(exclusive)
        Random rand = new Random();
        Color temp = new Color(rand.nextInt(255) + 1,
                rand.nextInt(255) + 1,
                               rand.nextInt(255) + 1) ;
        return temp;
    }

    /**
     * Find a pylon in the main collection.
     * @param indexNumber
     * number of the pylon
     * @return
     * pylon found in the main collection
     * @throws Exception
     * there is no Pylons to be found
     */
    public static Pylon findPylonWithIndexNumber(int indexNumber){
        Iterator iter = gw.getIterator();

        while(iter.hasNext()) {
            GameObject mObj = (GameObject) iter.getNext();
                if (mObj instanceof Pylon) {
                    if(((Pylon) mObj).getIndexNumber() == indexNumber) {
                        return ((Pylon) mObj);
                }
            }
        }
        return null;
    }

    /**
     *
     * @return
     * the first FuelCan in the main collection
     * @throws Exception
     * there is no FuelCans to be found
     */
    public static FuelCan findTheFirstFuelCan() throws Exception {
        Iterator iter = gw.getIterator();

        while(iter.hasNext()) {
            GameObject mObj = (GameObject) iter.getNext();
            if(mObj instanceof FuelCan){
                return ((FuelCan) mObj);

            }
        }
        throw new Exception("There are no fuelCans in the game");
    }

    /**
     * Find the number of pylons in the collection of game objects
     * @return
     * number of pylons in collection of game objects
     */
    public static int findTheNumberOfPylons(){

        int tempNumOfPylons = 0;

        Iterator iter = gw.getIterator();

        while(iter.hasNext()) {
            GameObject mObj = (GameObject) iter.getNext();
            if(mObj instanceof Pylon){
                tempNumOfPylons++;

            }
        }
        return tempNumOfPylons;
    }

    public static Clip retrieveSoundFromResources(String soundName) {

        String slash = File.separator;
        String pathToResources = ".." + slash + ".." + slash + "resources" + slash + "sounds" + slash;

        AudioInputStream inputStream = null;

        Clip clip = null;
        try {
            inputStream = AudioSystem.getAudioInputStream(gw.getClass().getResource(pathToResources + soundName));
            clip = AudioSystem.getClip();
            clip.open(inputStream);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return clip;
    }


    /**
     * Find and return all Pylons in the game world.
     * @return return all pylons in GW
     */
    public static Vector<Pylon> getAllPylons(){
        Vector<Pylon> allPylons = new Vector<>();
        Iterator iterForPylons = gw.getIterator();
        while(iterForPylons.hasNext()){
            GameObject temp = (GameObject)iterForPylons.getNext();
            if(temp instanceof Pylon){
                allPylons.addElement((Pylon)temp);
            }
        }
        return allPylons;
    }


    /**
     * Get the image resources
     * @return image resource folder
     */
    public static String getPathToImgResources(){

        String slash = File.separator;
        return "." +slash+"resources"+slash+ "img"+slash;
    }

    /**
     * Get the sound resources
     * @return sound resource folder
     */
    public static String getPathToSoundResources(){
        String slash = File.separator;
        return "." + slash +"resources" + slash + "sounds" + slash;
    }


}
