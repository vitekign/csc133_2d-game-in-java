/* Created by Victor Ignatenkov on 2/9/15 */

package a4.objects;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

/* The parent of all game objects.
 * Contains the most general fields and
 * methods, which are relevant to all
 * game objects */

public class GameObject {

    protected float X;
    protected float Y;

    protected List<GameObject> objectsCollidedWith;

    protected Color color;

    protected AffineTransform myTranslationMatrix;
    protected AffineTransform myRotationMatrix;
    protected AffineTransform myScaleMatrix;

    public void rotate(double degrees){
        myRotationMatrix.rotate(Math.toRadians(degrees));
    }

    public void scale (double sx, double sy){
        myScaleMatrix.scale(sx, sy);
    }

    public void translate(double dx, double dy){
        myTranslationMatrix.translate(dx, dy);
    }

    public List<GameObject> getObjectsCollidedWith() {
        return objectsCollidedWith;
    }

    public void setToIdentity(){
        myRotationMatrix.setToIdentity();
        myScaleMatrix.setToIdentity();
        myTranslationMatrix.setToIdentity();
    }

    public int getZIndex(){
        return 0;
    }

    /* Object can have their color changed */
    GameObject(Color color){
        this.color = color;

        myTranslationMatrix = new AffineTransform();
        myRotationMatrix = new AffineTransform();
        myScaleMatrix = new AffineTransform();
    }

    public void changeColor(Color color){
            this.color = color;
        }

    public Location getLocation(){
        return new Location(X, Y);
    }

    Color getColor(){
        return color;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }

    public void setX(float x) {
        X = x;
    }

    @Override
    public String toString() {
        return " loc=" + (int)X +
                "," + (int)Y +
                " color=[" + color.getRed() + " ," + color.getGreen() + ", "
                + color.getBlue() + "]";
    }
}
