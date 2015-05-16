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
import java.util.Vector;

/**
 * OilSlick class.
 * Oil Slicks - are places where cars
 * lose a part of their normal functionality.
 */
public class OilSlick extends Fixed implements IDrawable, ICollider {


    private float width;
    private float length;
    private GameWorld gw;
    public static int zIndex;

    Image imageRes;

    public OilSlick(Location location, float width, float length, Color color, GameWorld gw){
        super(color);

        this.gw = gw;

        objectsCollidedWith = new Vector<>();



        this.width = width;
        this.length = length;

        color = Color.black;

        myTranslationMatrix.translate(location.getX(), (int) location.getY());



        String pathToResources = Services.getPathToImgResources();
        File file = new File(pathToResources + "oilSlick.png");

        try {
            imageRes = ImageIO.read(file);
        } catch (Exception e){
            System.out.println("The picture for Oil Slick wasn't found");
        }

        scale(1,-1);
    }





    public int getZIndex(){
        return OilSlick.zIndex;
    }

    /**
     * @return
     * width
     */
    public float getWidth() {
        return width;
    }

    /**
     * Retrieve height.
     * @return
     * height
     */
    public float getLength() {
        return length;
    }





    @Override
    public String toString() {
        return "OilSlick:" +
                super.toString() +
                " width="   + (int)width +
                " length="  + (int)length;
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform saveAt = g.getTransform();

        g.transform(myTranslationMatrix);
        g.transform(myScaleMatrix);

        g.setColor(color);
        g.drawImage(imageRes,-50, -50, (int)100, (int)100, null);
        // g.drawImage(imageRes,(int)getX()-(int)(width/2), (int)getY()-(int)(length/2), (int)width, (int)length, null);
        //g.fillOval((int)getX()-(int)(width/2), (int)getY()-(int)(length/2), (int)width, (int)length);
        g.setColor(Color.black);

        g.setTransform(saveAt);
    }


    @Override
    public double getX() {
       return myTranslationMatrix.getTranslateX();
    }

    @Override
    public double getY() {
       return myTranslationMatrix.getTranslateY();
    }

    @Override
    public Location getLocation() {
       return new Location((float)getX(), (float)getY());
    }

    /**
     * The logic which detects the collision
     * with other object in the Game World
     * @param obj the other object
     * @return true if collision has happened
     */
    @Override
    public boolean collidesWith(ICollider obj) {
        double distX = this.getX() - ((GameObject)obj).getX();
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
     * Handle collision of the Oil Slick with other Game Objects.
     * @param otherObject
     */
    @Override
    public void handleCollision(ICollider otherObject) {
        if(otherObject instanceof Car && !(otherObject instanceof NPCCar)){
            if(!objectsCollidedWith.contains((GameObject)otherObject)){
                objectsCollidedWith.add((GameObject)otherObject);
            }
        }
    }

    @Override
    public float getDistanceOfReference() {
        //return (width + length)/2;
        return 50;
    }
}
