package a1.objects;

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

    private static Vector<GameObject> theWorldVector;



    private Services(){
    }

    public static void supplyServicesWithCollectionOfObjects( Vector<GameObject> collection)
    {
        theWorldVector  = collection;
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

        for (int i=0; i<theWorldVector.size(); i++) {
            if (theWorldVector.elementAt(i) instanceof Pylon) {
                Pylon temp = (Pylon) theWorldVector.elementAt(i);
                if(temp.getIndexNumber() == indexNumber){
                    return temp;
                }
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
        FuelCan temp = null;
        for (int i=0; i<theWorldVector.size(); i++) {
            if (theWorldVector.elementAt(i) instanceof FuelCan) {
                temp = (FuelCan) theWorldVector.elementAt(i);
                return temp;
            }
        }
        throw new Exception("There are no fuelCans in the game");
    }


}
