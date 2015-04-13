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

    public OilSlick(Location location, float width, float length, Color color, GameWorld gw){
        super(color);

        this.gw = gw;

        objectsCollidedWith = new Vector<>();


        this.X = location.getX();
        this.Y = location.getY();
        this.width = width;
        this.length = length;
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



        g.fillOval((int)getX()-(int)(width/2), (int)getY()-(int)(length/2), (int)width, (int)length);
        g.setColor(Color.black);
    }


    /*
     * Confirms to ICollider
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
            System.out.println("Just collided Oil Slick");
            //gw.gameObjectsToDelete.add((GameObject)this);
            if(!objectsCollidedWith.contains((GameObject)otherObject)){
                objectsCollidedWith.add((GameObject)otherObject);
                ((GameObject)otherObject).objectsCollidedWith.add(this);
            }
            gw.carCollideWithCar(null);
        }
    }

    @Override
    public float getDistanceOfReference() {
        return (width + length)/2;
    }
}
