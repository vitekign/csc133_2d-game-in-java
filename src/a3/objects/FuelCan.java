package a3.objects;

/**
 * Created by Victor Ignatenkov on 2/14/15.
 */

import java.awt.*;

/**
 * FuelCan class.
 * Serves as a means for a car to refill its tank.
 *
 * The size of a fuel can corresponds to the amount of fuel it contains.
 */
public class FuelCan extends Fixed implements IDrawable {

    private float size;

    public FuelCan(Location location, float size, Color color){
        super(color);

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
        g.drawString("FuelCan " + (int)getSize(), (int)getX(), (int)getY());
    }
}
