package a3.objects;

/**
 * Created by Victor Ignatenkov on 2/14/15.
 */

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

    public OilSlick(Location location, float width, float length, Color color){
        super(color);

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

    }


    /*
     * Confirms to ICollider
     */
    @Override
    public boolean collidesWith(ICollider obj) {
        return false;
    }

    @Override
    public void handleCollision(ICollider otherObject) {

    }

    @Override
    public float getDistanceOfReference() {
        return (width + length)/2;
    }
}
