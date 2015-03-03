package a1.objects;

/**
 * Created by Victor Ignatenkov on 2/9/15.
 */

/**
 * Location - class for holding x and y
 * values.
 */
public class Location {

    Location(float x, float y){
        this.X = x;
        this.Y = y;
    }
    private float X;
    private float Y;

    /**
     * @return
     * X value
     */
    public float getX() {
        return X;
    }

    /**
     * Set new value for X
     * @param x
     * new value for X
     */
    public void setX(float x) {
        X = x;
    }

    /**
     *
     * @return
     * Y value
     */
    public float getY() {
        return Y;
    }

    /**
     * Set new value for Y
     * @param y
     * new value for Y
     */
    public void setY(float y) {
        Y = y;
    }
}
