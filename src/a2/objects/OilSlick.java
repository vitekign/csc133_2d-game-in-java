package a2.objects;

/**
 * Created by Victor Ignatenkov on 2/14/15.
 */

import java.awt.*;

/**
 * OilSlick class.
 * Oil Slicks - are places where cars
 * lose a part of their normal functionality.
 */
public class OilSlick extends Fixed {


    private float width;
    private float length;

    public OilSlick(Location location, float width, float length, Color color){
        super(color);

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
}
