package a2.objects;

import a2.model.GameWorld;
import a2.model.Iterator;

import java.awt.*;
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
    public static Pylon findPylonWithIndexNumber(int indexNumber) throws Exception {
        Iterator iter = gw.getIterator();

        while(iter.hasNext()) {
            GameObject mObj = (GameObject) iter.getNext();
                if(((Pylon) mObj).getIndexNumber() == indexNumber){
                    return ((Pylon) mObj);

            }
        }
        throw new Exception("There are no pylons");
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

}
