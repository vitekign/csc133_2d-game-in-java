package a4.app.utilities;

import a4.model.GameWorld;
import a4.model.Iterator;
import a4.objects.FuelCan;
import a4.objects.GameObject;
import a4.objects.OilSlick;
import a4.objects.Pylon;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

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
    private static AffineTransform theVTM;



    public static HashMap<String, Image> images;

    public static String pathToResources = Services.getPathToImgResources();


    /**
     * Load all images for the game.
     * @param name
     * @return
     */
    public static Image getImage(String name) {



        Iterable<Path> dirs = FileSystems.getDefault().getRootDirectories();
        for(Path nameOfDir : dirs){
            System.out.println(nameOfDir);
        }



        if (images == null) {
            initServices();

            //TODO use reflection to go through the folder and find all files
            List<String> imageNames = Arrays.asList("asphalt.png",
                   "asphalt_light copy.jpg", "asphalt_light.jpg", "asphalt_light.png",
                    "bird copy.png", "bird.png", "car.png",
                    "fuelcan copy.png", "fuelcan.png", "fuelcan_clicked.png",
                    "oilSlick.png", "pylon.png", "pylon_pressed.png");

            for(String imageName : imageNames){
                File file = new File(pathToResources + imageName);

                Image tempImage = null;

                try {
                    tempImage = ImageIO.read(file);
                } catch (Exception e) {
                    System.out.println("The picture for " + imageName + " wasn't found");
                }

                images.put(imageName, tempImage);

            }
        }
        if(images.containsKey(name)){
            return images.get(name);
        }
        return null;
    }



    private Services() {
    }


    public static void initServices(){
        images = new HashMap<>();
    }


    public static void supplyServicesWithGameWorld(GameWorld gameWorld) {
        gw = gameWorld;
    }


    /**
     * @return Returns a randomly generated java Color
     */

    public static Color generateRandomColor() {
        //(inclusive)(exclusive)
        Random rand = new Random();
        Color temp = new Color(rand.nextInt(255) + 1,
                rand.nextInt(255) + 1,
                rand.nextInt(255) + 1);
        return temp;
    }

    /**
     * Find a pylon in the main collection.
     *
     * @param indexNumber number of the pylon
     * @return pylon found in the main collection
     * @throws Exception there is no Pylons to be found
     */
    public static Optional<Pylon> findPylonWithIndexNumber(int indexNumber) {
        Iterator iter = gw.getIterator();

        while (iter.hasNext()) {
            GameObject mObj = (GameObject) iter.getNext();
            if (mObj instanceof Pylon) {
                if (((Pylon) mObj).getIndexNumber() == indexNumber) {
                    return Optional.of((Pylon) mObj);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * @return the first FuelCan in the main collection
     * @throws Exception there is no FuelCans to be found
     */
    public static FuelCan findTheFirstFuelCan() throws Exception {
        Iterator iter = gw.getIterator();

        while (iter.hasNext()) {
            GameObject mObj = (GameObject) iter.getNext();
            if (mObj instanceof FuelCan) {
                return ((FuelCan) mObj);

            }
        }
        throw new Exception("There are no fuelCans in the game");
    }

    /**
     * Find the number of pylons in the collection of game objects
     *
     * @return number of pylons in collection of game objects
     */
    public static int findTheNumberOfPylons() {

        int tempNumOfPylons = 0;

        Iterator iter = gw.getIterator();

        while (iter.hasNext()) {
            GameObject mObj = (GameObject) iter.getNext();
            if (mObj instanceof Pylon) {
                tempNumOfPylons++;

            }
        }
        return tempNumOfPylons;
    }


    /**
     * Find and return all Pylons in the game world.
     *
     * @return return all pylons in GW
     */
    public static Vector<Pylon> getAllPylons() {
        Vector<Pylon> allPylons = new Vector<>();
        Iterator iterForPylons = gw.getIterator();
        while (iterForPylons.hasNext()) {
            GameObject temp = (GameObject) iterForPylons.getNext();
            if (temp instanceof Pylon) {
                allPylons.addElement((Pylon) temp);
            }
        }
        return allPylons;
    }


    /**
     * Get the image resources
     *
     * @return image resource folder
     */
    public static String getPathToImgResources() {

        String slash = File.separator;
        return "." + slash + "resources" + slash + "img" + slash;
    }

    /**
     * Get the sound resources
     *
     * @return sound resource folder
     */
    public static String getPathToSoundResources() {
        String slash = File.separator;
        return "." + slash + "resources" + slash + "sounds" + slash;
    }

    /**
     * Give to the Services a reference
     * of the VTM in the game.
     *
     * @param vtm
     */
    public static void supplyServicesWithVTM(AffineTransform vtm) {
        theVTM = vtm;
    }

    public static AffineTransform getTheVTM() {
        return theVTM;
    }


    /**
     * Get an inverse of the VTM matrix in the Game
     *
     * @return
     */
    //public static Optional<AffineTransform> getInverseOfVTM(){
    public static AffineTransform getInverseOfVTM() {
        AffineTransform inverseVTM;
        try {
            inverseVTM = theVTM.createInverse();
            return inverseVTM;
        } catch (NoninvertibleTransformException e) {
            System.out.println("Cannot inverse the matrix: " + e.getMessage());
        }
        return null;
    }


    public static Point2D applyInverseAndGetPoint(MouseEvent lastMouseEvent) {

        AffineTransform inverseOfVTM = getInverseOfVTM();

        Point2D mouseScreenLocation = new Point();
        mouseScreenLocation.setLocation(lastMouseEvent.getX(), lastMouseEvent.getY());

        //  mouseScreenLocation = theVTM.transform(mouseScreenLocation, null);
        mouseScreenLocation = inverseOfVTM.transform(mouseScreenLocation, null);

        return mouseScreenLocation;
    }




/*
    public static boolean isThereOilSlick(Point2D point2d) {
        Iterator iter = gw.getIterator();
        while (iter.hasNext()) {
            GameObject temp = (GameObject) iter.getNext();
            if (temp instanceof OilSlick) {
                if (((OilSlick) temp).contains(point2d)) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }
    */
}
