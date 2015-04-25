package a4.objects;

/**
 * Created by Victor Ignatenkov on 2/14/15.
 */

import a4.app.utilities.Services;
import a4.model.GameWorld;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
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
    private int timer;

    public static int zIndex;


    private boolean isSelected;

    final static int ADDITIONAL_WIDTH_LENGTH = 20;


    public FuelCan(Location location, float size, Color color, GameWorld gw){
        super(color);

        this.gw = gw;

        objectsCollidedWith = new Vector<>();

        this.X = location.getX();
        this.Y = location.getY();

        this.size = size;

        timer = (int)size;


        try {
            String pathToResources = Services.getPathToImgResources();
            String imgName = "fuelcan.png";

            imageRes= ImageIO.read(new File(pathToResources+imgName));

        }catch (IOException ex){
            System.out.println("An error happened: " + ex.getMessage());
        }




        myRotationMatrix = new AffineTransform();
        myTranslationMatrix = new AffineTransform();
        myScaleMatrix = new AffineTransform();



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
    public boolean contains(Point p) {

        int px = (int) p.getX();
        int py = (int) p.getY();
        int xLoc = (int)getX();
        int yLoc = (int)getY();

        if((px >= xLoc - (this.getDistanceOfReference())) && (px <= xLoc + (this.getDistanceOfReference()))
                && (py >= yLoc - (this.getDistanceOfReference()))&& (py <= yLoc + (this.getDistanceOfReference())))
            return true;
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


        myTranslationMatrix.translate((int) getX() - (int) (width / 2), (int) getY() - (int) (length / 2));
        myRotationMatrix.rotate(Math.toRadians(180));

        AffineTransform saveAt = g2d.getTransform();

        g2d.transform(myTranslationMatrix);
        g2d.transform(myRotationMatrix);


        if(gw.getTime()%50 == 0 && !gw.isItInPause()){
            timer--;
            size = timer;
        }

        if(isSelected){
            g2d.setColor(new Color(13, 66, 160));
            g2d.fillRect(0,0,
                    (int) size + ADDITIONAL_WIDTH_LENGTH, (int) size + ADDITIONAL_WIDTH_LENGTH);
        } else {
            g2d.drawImage(imageRes, 0,0,
                    (int) size + ADDITIONAL_WIDTH_LENGTH, (int) size + ADDITIONAL_WIDTH_LENGTH, null);
        }

        g2d.setColor(Color.white);
        g2d.fillOval(0, 0, 5, 5);


        g2d.setColor(new Color(234, 32, 0));
        myScaleMatrix.scale(-1, 1);
        g2d.transform(myScaleMatrix);
        g2d.drawString(String.valueOf((int) getSize()), 0, 0);





        setToIdentity();
        g2d.setTransform(saveAt);
    }


    /*
     * ICollider Implementation
     */


    @Override
    public boolean collidesWith(ICollider obj) {
        float distX = this.getX() - ((GameObject)obj).getX();
        float distY = this.getY() - ((GameObject)obj).getY();
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
