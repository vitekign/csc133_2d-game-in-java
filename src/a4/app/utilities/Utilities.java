package a4.app.utilities;

import a4.model.GameWorld;
import a4.model.Iterator;
import a4.objects.FuelCan;
import a4.objects.GameObject;
import a4.objects.Pylon;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.*;
import java.util.List;

/* Created by Victor Ignatenkov on 2/22/15 */
/**
 * Class Utilities provides various general-purpose
 * services, which don't belong to any specific class.
 *
 * Make constructor private so it behaves
 * and looks like a static class.
 */

public class Utilities {


    public static final String IMAGE_NAME_PYLON = "farm_1.png";
    public static final String IMAGE_NAME_PYLON_PRESSED = "farm_1.png";
    public static final String IMAGE_NAME_BIRD = "bird_1.png";
    public static final String IMAGE_NAME_MISSILE = "missile_1.png";


    public static final String BASE_FONT = "Pipe Dream";


    private static GameWorld gw;
    private static AffineTransform theVTM;
    private static HashMap<String, Image> images;
    private static String pathToResources = Utilities.getPathToImgResources();

    private Utilities() {}

    public static String getPathToImgResources() {
        String slash = File.separator;
        return "." + slash + "resources" + slash + "img" + slash;
    }


    public static void loadImages() {

        List<String> pictureNames = getPictureNamesDynamically();

        for(String imageName : pictureNames){
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

    private static List<String> getPictureNamesDynamically() {
        File imageFolder = new File(pathToResources);
        return Arrays.asList(imageFolder.list());
    }


    public static Image loadImage(String name) {
        if (images == null) {
            initServices();
            loadImages();
        }
        if(images.containsKey(name)){
            return images.get(name);
        }
        return null;
    }

    public static void initServices(){
        images = new HashMap<>();
    }

    public static void supplyServicesWithGameWorld(GameWorld gameWorld) {
        gw = gameWorld;
    }

    public static Color generateRandomColor() {
        //(inclusive)(exclusive)
        Random rand = new Random();
        return new Color(rand.nextInt(255) + 1,
                rand.nextInt(255) + 1,
                rand.nextInt(255) + 1);
    }

    public static Optional<Pylon> findPylonWithIndexNumber(int indexNumber) {
        Iterator iterator = gw.getIterator();

        while (iterator.hasNext()) {
            GameObject mObj = (GameObject) iterator.getNext();
            if (mObj instanceof Pylon) {
                if (((Pylon) mObj).getIndexNumber() == indexNumber) {
                    return Optional.of((Pylon) mObj);
                }
            }
        }
        return Optional.empty();
    }

    public static Optional<FuelCan> findTheFirstFuelCan() {
        Iterator iterator = gw.getIterator();
        while (iterator.hasNext()) {
            GameObject mObj = (GameObject) iterator.getNext();
            if (mObj instanceof FuelCan) {
                return Optional.of((FuelCan) mObj);
            }
        }
       return Optional.empty();
    }

    public static int findTheNumberOfPylons() {
        int tempNumOfPylons = 0;

        Iterator iterator = gw.getIterator();

        while (iterator.hasNext()) {
            GameObject mObj = (GameObject) iterator.getNext();
            if (mObj instanceof Pylon) {
                tempNumOfPylons++;
            }
        }
        return tempNumOfPylons;
    }

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



    public static String getPathToSoundResources() {
        String slash = File.separator;
        return "." + slash + "resources" + slash + "sounds" + slash;
    }

    public static void supplyUtilitiesWithVTM(AffineTransform vtm) {
        theVTM = vtm;
    }

    /* Get an inverse of the VTM matrix in the Game */
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
