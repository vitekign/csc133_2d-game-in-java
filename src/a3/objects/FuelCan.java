package a3.objects;

/**
 * Created by Victor Ignatenkov on 2/14/15.
 */

import java.awt.*;
import java.util.Vector;

/**
 * FuelCan class.
 * Serves as a means for a car to refill its tank.
 *
 * The size of a fuel can corresponds to the amount of fuel it contains.
 */
public class FuelCan extends Fixed implements IDrawable, ICollider{

    private float size;

    public FuelCan(Location location, float size, Color color){
        super(color);

        objectsCollidedWith = new Vector<>();

        this.X = location.getX();
        this.Y = location.getY();

        this.size = size;
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

    @Override
    public void draw(Graphics g) {

        int width = (int)size;
        int length = (int)size;


        g.fillRect((int) getX() - (int) (width / 2), (int) getY() - (int) (length / 2), (int) width, (int) length);
        g.drawOval((int) getX(), (int) getY(), 1, 1);
        g.setColor(Color.white);
        g.drawString(String.valueOf((int)getSize()), (int) getX()-(width/2), (int) getY()-(int)(length/2));
        g.setColor(Color.black);
    }


    /*
     * ICollider Implementation
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
        return size/2;
    }
}
