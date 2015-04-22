package a3.objects;

/**
 * Created by Victor Ignatenkov on 2/14/15.
 */

import a3.model.GameWorld;

import java.awt.*;
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


    public OilSlick(Location location, float width, float length, Color color, GameWorld gw){
        super(color);

        this.gw = gw;

        objectsCollidedWith = new Vector<>();


        this.X = location.getX();
        this.Y = location.getY();
        this.width = width;
        this.length = length;

        color = Color.black;
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
    public void draw(Graphics g) {

        g.setColor(color);
        g.fillOval((int)getX()-(int)(width/2), (int)getY()-(int)(length/2), (int)width, (int)length);
        g.setColor(Color.black);
    }


    /**
     * The logic which detects the collision
     * with other object in the Game World
     * @param obj the other object
     * @return true if collision has happened
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
        return (width + length)/2;
    }
}
