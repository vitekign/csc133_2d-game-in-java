package a3.objects;

/**
 * Created by Victor Ignatenkov on 2/14/15.
 */

import a3.app.utilities.Services;
import a3.model.GameWorld;

import javax.imageio.ImageIO;
import java.awt.*;
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

        if((px >= xLoc - (this.getSize()/2)) && (px <= xLoc + (this.getSize()/2))
                && (py >= yLoc - (this.getSize()/2))&& (py <= yLoc + (this.getSize()/2)))
            return true;
        else
            return false;
    }



    public int getTimer(){
        return timer;
    }


    @Override
    public void draw(Graphics g) {

        int width = (int)size + ADDITIONAL_WIDTH_LENGTH;
        int length = (int)size + ADDITIONAL_WIDTH_LENGTH;


        if(gw.getTime()%50 == 0 && !gw.isItInPause()){
            timer--;
            size = timer;
        }

        if(isSelected){
            g.setColor(new Color(13, 66,160));
            g.fillRect( (int) getX() - (int) (width / 2), (int) getY() - (int) (length / 2), (int)size, (int)size );
        } else {
            g.drawImage(imageRes, (int) getX() - (int) (width / 2), (int) getY() - (int) (length / 2),
                    (int)size + ADDITIONAL_WIDTH_LENGTH , (int)size+ADDITIONAL_WIDTH_LENGTH , null);
        }


        g.setColor(new Color(234, 32, 0));
        g.drawString(String.valueOf((int)getSize()), (int) getX(), (int) getY());


        g.setColor(Color.white);
        g.fillOval( (int) getX() , (int) getY() ,  2, 2);
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

    @Override
    public void handleCollision(ICollider otherObject) {
        if(otherObject instanceof Car && !(otherObject instanceof NPCCar)){
            System.out.println("Just collided Fuel Can");
            gw.gameObjectsToDelete.add((GameObject)this);
            if(!objectsCollidedWith.contains((GameObject)otherObject)){
                objectsCollidedWith.add((GameObject)otherObject);


                if(!gw.gameObjectsToDelete.contains(this)) {
                    ((GameObject) otherObject).objectsCollidedWith.add(this);
                }

            }
            if(gw.isSound())
                ((Car)otherObject).playSoundForFuelEating();
            gw.pickUpFuelCan(this);
        }
    }

    @Override
    public float getDistanceOfReference() {
        return size/2;
    }
}
