/*Created by Victor Ignatenkov on 2/14/15.*/
package a4.objects;
import a4.app.utilities.Utilities;
import a4.model.GameWorld;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Vector;

/* Serves as a means for a car to refill its tank. */
public class FuelCan extends Fixed implements IDrawable, ICollider, ISelectable{

    final static int ADDITIONAL_WIDTH_LENGTH = 20;

    private float size;
    private GameWorld gw;

    private int timer;
    public static int zIndex;
    private boolean isSelected;

    Image imageRes;
    Image imageResClicked;

    public FuelCan(Location location, float size, Color color, GameWorld gw){
        super(color);

        this.gw = gw;

        objectsCollidedWith = new Vector<>();

        translate(location.getX(), location.getY());
        myRotationMatrix.rotate(Math.toRadians(180));

        myScaleMatrix.scale(-1.1,1.1);

        double addAngle = (new Random().nextDouble() * 44) - 22;
        rotate(addAngle);

        this.size = size;
        timer = (int)size;

        imageRes = Utilities.loadImage("hay_2.png");
        imageResClicked = Utilities.loadImage("fuelcan_clicked.png");
    }

    @Override
    public boolean contains(Point2D p) {
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


        if((Math.abs(px) < this.getDistanceOfReference() && Math.abs(py) < this.getDistanceOfReference())){
            return true;
        }
        else
            return false;
    }



    //TODO Refactor to Graphics2d
    @Override
    public void draw(Graphics2D g2d) {

        int width = (int)size + ADDITIONAL_WIDTH_LENGTH;
        int length = (int)size + ADDITIONAL_WIDTH_LENGTH;

        AffineTransform saveAt = g2d.getTransform();

        g2d.transform(myTranslationMatrix);
        g2d.transform(myRotationMatrix);
        g2d.transform(myScaleMatrix);

        if(gw.getTimeInTicks()%50 == 0 && !gw.isItInPause()){
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

        g2d.setFont(new Font(Utilities.BASE_FONT, Font.BOLD, 10));
        g2d.setColor(new Color(255, 255, 255));
        myScaleMatrix.scale(-1, 1);
        g2d.transform(myScaleMatrix);
        g2d.drawString(String.valueOf((int) getSize()), -8, 3);

        myScaleMatrix.scale(-1, 1);

       // setToIdentity();
        g2d.setTransform(saveAt);
    }

    @Override
    public boolean didCollideWithAnotherObject(ICollider obj) {
        double distX = this.getX() - ((GameObject) obj).getX();
        double distY = this.getY() - ((GameObject)obj).getY();
        float distanceBtwnCenters = (float) Math.sqrt(distX * distX + distY * distY);

        return (this.getDistanceOfReference() + obj.getDistanceOfReference() > distanceBtwnCenters);
    }

    /**
     * Handle collision of the Car with other Game Objects.
     * @param otherObject
     */
    @Override
    public void handleCollision(ICollider otherObject) {
        if(otherObject instanceof Car && !(otherObject instanceof NPCCar)){
            gw.gameObjectsToBeRemoved.add(this);
            if(!objectsCollidedWith.contains(otherObject)){
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


    @Override
    public String toString() {
        return "FuelCan: " +
                super.toString() +
                " size " + (int)this.size;
    }

    public int getTimer(){
        return timer;
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
}
