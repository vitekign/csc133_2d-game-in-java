/**
 * Created by Victor Ignatenkov on 4/28/15.
 */

package a4.objects.character_car;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Abstract class with a set of essential methods
 * to work with Affine transformations.
 */

public abstract class AffineObject {

    protected Color myColor;
    protected AffineTransform myTranslation;
    protected AffineTransform myRotation;
    protected AffineTransform myScale;

    /**
     * Rotate the corresponding matrix.
     * @param degrees degrees
     */
    public void rotate(double degrees){
        myRotation.rotate(Math.toRadians(degrees));
    }

    /**
     * Scale the corresponding matrix.
     * @param sx x coordinate.
     * @param sy y coordinate.
     */
    public void scale(double sx, double sy){
        myScale.scale(sx, sy);
    }

    /**
     * Translate the corresponding matrix.
     * @param dx x coordinate.
     * @param dy y coordinate.
     */
    public void translate(double dx, double dy){
        myTranslation.translate(dx, dy);
    }

    /**
     * Set to identity all of the Affine Matrices
     * of the object.
     */
    public void setToIdentity(){
        myTranslation.setToIdentity();
        myScale.setToIdentity();
        myRotation.setToIdentity();
    }

    /**
     * Obligatory draw method, for objects to
     * confirm to.
     * @param g2d
     */
    abstract public void draw (Graphics2D g2d);
}
