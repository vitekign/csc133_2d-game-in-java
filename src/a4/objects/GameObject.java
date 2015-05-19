package a4.objects;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Vector;

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

    /**
     *  Define Affine properties with corresponding setters.
     */

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

    public void setToIdentity(){
        myRotationMatrix.setToIdentity();
        myScaleMatrix.setToIdentity();
        myTranslationMatrix.setToIdentity();
    }
    /**************************************************/



    //TODO refactor to private
    public Vector<GameObject> objectsCollidedWith;

    //TODO refactor to private
    public boolean toDelete;
    public boolean hasCollided;

    protected Color color;



    public int getZIndex(){
        return 0 ;
    }




    /**
     * Default ability of all object to have their color changed.
     */
    GameObject(Color color){
        this.color = color;


        myTranslationMatrix = new AffineTransform();
        myRotationMatrix = new AffineTransform();
        myScaleMatrix = new AffineTransform();





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
}
