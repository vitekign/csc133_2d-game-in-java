package a4.objects;

/**
 * Created by Victor Ignatenkov on 2/14/15.
 */

import a4.app.utilities.Services;
import a4.model.GameWorld;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

/**
 * FuelCan class.
 * Serves as a means for a car to refill its tank.
 *
 * The size of a fuel can corresponds to the amount of fuel it contains.
 */
public class FuelCan extends Fixed implements IDrawable, ICollider, ISelectable{

    private float size;
    private GameWorld gw;

    Image imageRes;
    Image imageResClicked;

    private int timer;

    public static int zIndex;


    private boolean isSelected;

    final static int ADDITIONAL_WIDTH_LENGTH = 20;


    @Override
    public Location getLocation() {
        return new Location((float)getX(), (float)getY());
    }

    @Override
    public double getX() {
        return  myTranslationMatrix.getTranslateX();
    }

    @Override
    public double getY() {
        return  myTranslationMatrix.getTranslateY();
    }

    public FuelCan(Location location, float size, Color color, GameWorld gw){
        super(color);

        this.gw = gw;

        objectsCollidedWith = new Vector<>();


        /*

        this.X = location.getX();
        this.Y = location.getY();
*/
        translate(location.getX(), location.getY());
        myRotationMatrix.rotate(Math.toRadians(180));

        myScaleMatrix.scale(-1.1,1.1);

        double addAngle = (new Random().nextDouble() * 44) - 22;
        rotate(addAngle);

        this.size = size;

        timer = (int)size;


        try {
            String pathToResources = Services.getPathToImgResources();
            String imgName = "fuelcan.png";

            imageRes= ImageIO.read(new File(pathToResources+imgName));

        }catch (IOException ex){
            System.out.println("An error happened: " + ex.getMessage());
        }

        try {
            String pathToResources = Services.getPathToImgResources();
            String imgName = "fuelcan_clicked.png";

            imageResClicked= ImageIO.read(new File(pathToResources+imgName));

        }catch (IOException ex){
            System.out.println("An error happened: " + ex.getMessage());
        }



    }

    @Override
    public String toString() {
        return "FuelCan: " +
                super.toString() +
                " size " + (int)this.size;
    }

    /**
     * Return the amount of gas in the fuel can.
     * @return
     * amount of gas in the fuel can.
     */
    public float getSize() {
        return size;
    }

    public int getZIndex(){
        return FuelCan.zIndex;
    }

    /**
     * Confirms to ISelectable
     */
    @Override
    public void setSelected(boolean yesNo) {
        this.isSelected = yesNo;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public boolean contains(Point2D p) {

        //new stuff

        int xLoc = (int)getX();
        int yLoc = (int)getY();



        //get inverse
        //public void concatenate(AffineTransform Tx)
        //Cx'(p) = Cx(Tx(p))
        //first transforming p by Tx and then transforming the result by the original transform Cx

        AffineTransform temp = new AffineTransform();
        temp.setTransform(myTranslationMatrix);
        temp.concatenate(myRotationMatrix);
        temp.concatenate(myScaleMatrix);
        //apply the inverse to the point
        AffineTransform inverseVTM = null;
        try {
            inverseVTM = temp.createInverse();
        } catch (NoninvertibleTransformException e){
            System.out.println("Cannot inverse the matrix: " + e.getMessage());
        }

        Point2D mouseScreenLocation = new Point();
        mouseScreenLocation.setLocation(p.getX(), p.getY());

        //  mouseScreenLocation = theVTM.transform(mouseScreenLocation, null);
        mouseScreenLocation = inverseVTM.transform(mouseScreenLocation,null);


        int px = (int) Math.abs(mouseScreenLocation.getX());
        int py = (int) Math.abs(mouseScreenLocation.getY());


        //------------





        /*

        if((px >= xLoc - (this.getDistanceOfReference())) && (px <= xLoc + (this.getDistanceOfReference()))
                && (py >= yLoc - (this.getDistanceOfReference()))&& (py <= yLoc + (this.getDistanceOfReference())))
                */
        if((Math.abs(px) < this.getDistanceOfReference() && Math.abs(py) < this.getDistanceOfReference())){

            System.out.println("Mouse x location is: " + px);
            System.out.println("Mouse y location is: " + py);
            System.out.println("");
            return true;
        }

        else
            return false;
    }



    public int getTimer(){
        return timer;
    }


    //TODO Refactor to Graphics2d
    @Override
    public void draw(Graphics2D g2d) {

        int width = (int)size + ADDITIONAL_WIDTH_LENGTH;
        int length = (int)size + ADDITIONAL_WIDTH_LENGTH;


       //myTranslationMatrix.translate((int) getX() +  (width / 2), (int) getY() +  (length / 2));
       // myTranslationMatrix.translate((int) getX(), (int) getY());
       // myRotationMatrix.rotate(Math.toRadians(180));


        AffineTransform saveAt = g2d.getTransform();

        g2d.transform(myTranslationMatrix);
        g2d.transform(myRotationMatrix);
        g2d.transform(myScaleMatrix);


        if(gw.getTime()%50 == 0 && !gw.isItInPause()){
            timer--;
            size = timer;
        }

        if(isSelected){
            g2d.setColor(new Color(13, 66, 160));
            g2d.drawImage(imageResClicked,  (int) -(size + ADDITIONAL_WIDTH_LENGTH)/2,
                    (int) -(size + ADDITIONAL_WIDTH_LENGTH)/2,
                    (int) size + ADDITIONAL_WIDTH_LENGTH, (int) size + ADDITIONAL_WIDTH_LENGTH, null);
        } else {
            g2d.drawImage(imageRes,  (int) -(size + ADDITIONAL_WIDTH_LENGTH)/2,
                    (int) -(size + ADDITIONAL_WIDTH_LENGTH)/2,
                    (int) size + ADDITIONAL_WIDTH_LENGTH, (int) size + ADDITIONAL_WIDTH_LENGTH, null);
        }

      //  g2d.setColor(Color.white);
       // g2d.fillOval(0, 0, 5, 5);


        g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        g2d.setColor(new Color(255, 255, 255));
        myScaleMatrix.scale(-1, 1);
        g2d.transform(myScaleMatrix);
        g2d.drawString(String.valueOf((int) getSize()), -8, 3);

        myScaleMatrix.scale(-1, 1);




       // setToIdentity();
        g2d.setTransform(saveAt);
    }


    /*
     * ICollider Implementation
     */


    @Override
    public boolean collidesWith(ICollider obj) {
        double distX = this.getX() - ((GameObject) obj).getX();
        double distY = this.getY() - ((GameObject)obj).getY();
        float distanceBtwnCenters = (float) Math.sqrt(distX * distX + distY * distY);

        if((this.getDistanceOfReference() + obj.getDistanceOfReference() >
                distanceBtwnCenters)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Handle collision of the Car with other Game Objects.
     * @param otherObject
     */
    @Override
    public void handleCollision(ICollider otherObject) {
        if(otherObject instanceof Car && !(otherObject instanceof NPCCar)){
            System.out.println("Just collided Fuel Can");
            gw.gameObjectsToDelete.add((GameObject)this);
            if(!objectsCollidedWith.contains((GameObject)otherObject)){
                objectsCollidedWith.add((GameObject)otherObject);

            }
            if(gw.isSound())
                ((Car)otherObject).playSoundForFuelEating();
            gw.pickUpFuelCan(this);
        }
    }

    @Override
    public float getDistanceOfReference() {
        return (size+ADDITIONAL_WIDTH_LENGTH)/2;
    }
}
