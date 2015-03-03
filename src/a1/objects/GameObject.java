package a1.objects;

import java.awt.*;

/**
 * Created by Victor Ignatenkov on 2/9/15.
 */

/**
 * The parent class for all game objects.
 * Contains the most general fields and
 * methods, which are relevant to all
 * game objects.
 */

public class GameObject {

    protected float X;
    protected float Y;

    protected Color color;

    /**
     * Default ability of all object to have their color changed.
     */
    GameObject(Color color){
        this.color = color;
    }


    /**
     * Change the color of the object.
     * @param color
     * new color of the object
     */
    public void changeColor(Color color){
            this.color = color;
        }


    /**
     * Return current location.
     * @return
     * Location of the object.
     */
    public Location getLocation(){
        return new Location(X, Y);
    }

    /**
     * Returns current color.
     * @return
     * Color of the object.
     */
    Color getColor(){
        return color;
    }


    @Override
    public String toString() {
        return " loc=" + (int)X +
                "," + (int)Y +
                " color=[" + color.getRed() + " ," + color.getGreen() + ", "
                + color.getBlue() + "]";
    }
}
